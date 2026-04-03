import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, finalize, of, timeout } from 'rxjs';

import { AnimalService } from '../../core/services/animal.service';
import { ApostaService } from '../../core/services/aposta.service';
import { AuthService } from '../../core/services/auth.service';
import { UsuarioService } from '../../core/services/usuario.service';
import { Animal } from '../../shared/models/animal.model';
import { ApostaHistorico } from '../../shared/models/aposta-historico.model';
import { ApostaResponse } from '../../shared/models/aposta-response.model';
import { LoginResponse } from '../../shared/models/login-response.model';
import { UsuarioResponse } from '../../shared/models/usuario-response.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './dashboard.page.html',
  styleUrls: ['./dashboard.page.css']
})
export class DashboardPage implements OnInit {

  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);
  private readonly usuarioService = inject(UsuarioService);
  private readonly animalService = inject(AnimalService);
  private readonly apostaService = inject(ApostaService);

  usuarioLogado: LoginResponse | null = null;
  usuarioAtual: UsuarioResponse | null = null;

  animais: Animal[] = [];
  historico: ApostaHistorico[] = [];
  ultimaAposta: ApostaResponse | null = null;

  carregandoPagina = true;
  carregandoAposta = false;
  carregandoHistorico = false;

  erro = '';
  erroHistorico = '';
  sucesso = '';

  private pendenciasCarregamento = 0;

  form = this.fb.group({
    grupoAnimal: [null as number | null, [Validators.required]],
    valor: [null as number | null, [Validators.required, Validators.min(1)]]
  });

  ngOnInit(): void {

    this.usuarioLogado = this.authService.getUsuarioLogado();

    if (!this.usuarioLogado?.usuarioId) {
      this.logout();
      return;
    }

    this.carregarDashboard();
  }

  carregarDashboard(): void {

    if (!this.usuarioLogado?.usuarioId) {
      this.logout();
      return;
    }

    const usuarioId = this.usuarioLogado.usuarioId;

    this.carregandoPagina = true;
    this.carregandoHistorico = true;

    this.erro = '';
    this.erroHistorico = '';

    this.pendenciasCarregamento = 3;

    // carregar usuário
    this.usuarioService.buscarPorId(usuarioId)
      .pipe(
        timeout(8000),
        catchError((error) => {
          console.error('Erro ao buscar usuário:', error);

          this.erro = this.extrairMensagemErro(
            error,
            'Não foi possível carregar os dados do usuário.'
          );

          return of(null as UsuarioResponse | null);
        }),
        finalize(() => this.finalizarCarregamento())
      )
      .subscribe(usuario => {
        this.usuarioAtual = usuario;
      });

    // carregar animais
    this.animalService.listarAnimais()
      .pipe(
        timeout(8000),
        catchError((error) => {

          console.error('Erro ao buscar animais:', error);

          this.erro = this.extrairMensagemErro(
            error,
            'Não foi possível carregar lista de animais.'
          );

          return of([] as Animal[]);
        }),
        finalize(() => this.finalizarCarregamento())
      )
      .subscribe(animais => {

        this.animais = [...animais]
          .sort((a, b) => a.grupo - b.grupo);

      });

    // carregar histórico
    this.apostaService.listarHistorico(usuarioId)
      .pipe(
        timeout(8000),
        catchError((error) => {

          console.error('Erro ao buscar histórico:', error);

          this.erroHistorico = this.extrairMensagemErro(
            error,
            'Não foi possível carregar histórico.'
          );

          return of([] as ApostaHistorico[]);
        }),
        finalize(() => {

          this.carregandoHistorico = false;
          this.finalizarCarregamento();

        })
      )
      .subscribe(historico => {

        this.historico = historico;

      });
  }

  private finalizarCarregamento(): void {

    this.pendenciasCarregamento--;

    if (this.pendenciasCarregamento <= 0) {

      this.carregandoPagina = false;

    }
  }

  apostar(): void {

    if (this.form.invalid || !this.usuarioLogado?.usuarioId) {

      this.form.markAllAsTouched();
      return;

    }

    this.carregandoAposta = true;

    this.erro = '';
    this.sucesso = '';
    this.ultimaAposta = null;

    const payload = {

      usuarioId: this.usuarioLogado.usuarioId,
      grupoAnimal: Number(this.form.value.grupoAnimal),
      valor: Number(this.form.value.valor)

    };

    this.apostaService.registrarAposta(payload)
      .pipe(
        timeout(8000),
        finalize(() => {

          this.carregandoAposta = false;

        })
      )
      .subscribe({

        next: resposta => {

          this.ultimaAposta = resposta;

          this.sucesso = resposta.ganhou
            ? `Parabéns! Você ganhou R$ ${this.formatarMoeda(resposta.valorGanho)}`
            : 'Aposta registrada com sucesso. Tente novamente na próxima rodada.';

          this.form.reset({
            grupoAnimal: null,
            valor: null
          });

          this.atualizarUsuarioEHistorico();

        },

        error: error => {

          console.error('Erro ao apostar:', error);

          this.erro = this.extrairMensagemErro(
            error,
            'Não foi possível registrar aposta.'
          );

        }
      });
  }

  atualizarUsuarioEHistorico(): void {

    if (!this.usuarioLogado?.usuarioId) {
      return;
    }

    const usuarioId = this.usuarioLogado.usuarioId;

    this.carregandoHistorico = true;

    this.usuarioService.buscarPorId(usuarioId)
      .pipe(
        timeout(8000),
        catchError(() => of(null))
      )
      .subscribe(usuario => {

        if (usuario) {

          this.usuarioAtual = usuario;

        }
      });

    this.apostaService.listarHistorico(usuarioId)
      .pipe(
        timeout(8000),
        catchError(() => of([])),
        finalize(() => {

          this.carregandoHistorico = false;

        })
      )
      .subscribe(historico => {

        this.historico = historico;

      });
  }

  logout(): void {

    this.authService.logout();

    this.router.navigate(['/login']);

  }

  obterNomeAnimal(grupo: number | null | undefined): string {

    if (grupo == null) {
      return '-';
    }

    return this.animais
      .find(animal => animal.grupo === grupo)?.nome ?? '-';
  }

  formatarData(data: string | null | undefined): string {

    if (!data) {
      return '-';
    }

    const d = new Date(data);

    if (isNaN(d.getTime())) {
      return '-';
    }

    return d.toLocaleString('pt-BR');
  }

  formatarMoeda(valor: number | null | undefined): string {

    return Number(valor ?? 0)
      .toFixed(2)
      .replace('.', ',');

  }

  private extrairMensagemErro(error: any, fallback: string): string {

    return error?.error?.message
      || error?.error?.erro
      || error?.error
      || fallback;

  }

  get grupoAnimal() {

    return this.form.get('grupoAnimal');

  }

  get valor() {

    return this.form.get('valor');

  }
}