import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
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
  private readonly cdr = inject(ChangeDetectorRef);

  usuarioLogado: LoginResponse | null = null;
  usuarioAtual: UsuarioResponse | null = null;

  animais: Animal[] = [];
  historico: ApostaHistorico[] = [];
  ultimaAposta: ApostaResponse | null = null;

  carregandoPagina = true;
  carregandoAposta = false;
  carregandoHistorico = false;
  carregandoDeposito = false;

  erro = '';
  erroHistorico = '';
  sucesso = '';

  private pendenciasCarregamento = 0;

  tiposAposta = [
    { valor: 'GRUPO', rotulo: 'Grupo' },
    { valor: 'DEZENA', rotulo: 'Dezena' },
    { valor: 'CENTENA', rotulo: 'Centena' },
    { valor: 'MILHAR', rotulo: 'Milhar' },
    { valor: 'DUQUE_DE_DEZENA', rotulo: 'Duque de dezena' }
  ];

  form = this.fb.group({
    tipoAposta: ['GRUPO', [Validators.required]],
    grupoAnimal: [null as number | null, [Validators.required]],
    numeroApostado: [null as string | null],
    segundoNumero: [null as string | null],
    valor: [null as number | null, [Validators.required, Validators.min(1)]]
  });

  depositoForm = this.fb.group({
    valorDeposito: [null as number | null, [Validators.required, Validators.min(1)]]
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
        finalize(() => {
          this.finalizarCarregamento();
        })
      )
      .subscribe(usuario => {
        this.usuarioAtual = usuario;
        this.cdr.detectChanges();
      });

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
        finalize(() => {
          this.finalizarCarregamento();
        })
      )
      .subscribe(animais => {
        this.animais = [...animais]
          .sort((a, b) => a.grupo - b.grupo);

        this.cdr.detectChanges();
      });

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
          this.cdr.detectChanges();
        })
      )
      .subscribe(historico => {
        this.historico = historico;
        this.cdr.detectChanges();
      });
  }

  private finalizarCarregamento(): void {
    this.pendenciasCarregamento--;

    if (this.pendenciasCarregamento <= 0) {
      this.carregandoPagina = false;
      this.cdr.detectChanges();
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

    const tipoAposta = this.form.value.tipoAposta || 'GRUPO';

    if (!this.validarCamposAposta(tipoAposta)) {
      return;
    }

    const payload = {
      usuarioId: this.usuarioLogado.usuarioId,
      grupoAnimal: tipoAposta === 'GRUPO' ? Number(this.form.value.grupoAnimal) : null,
      valor: Number(this.form.value.valor),
      tipoAposta,
      numeroApostado: this.form.value.numeroApostado || null,
      segundoNumero: tipoAposta === 'DUQUE_DE_DEZENA' ? this.form.value.segundoNumero || null : null
    };

    this.apostaService.registrarAposta(payload)
      .pipe(
        timeout(8000),
        finalize(() => {
          this.carregandoAposta = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: resposta => {
          this.ultimaAposta = resposta;

          this.sucesso = resposta.ganhou
            ? `Parabéns! Você ganhou R$ ${this.formatarMoeda(resposta.valorGanho)}`
            : 'Aposta registrada com sucesso. Tente novamente na próxima rodada.';

          this.form.reset({
            tipoAposta: 'GRUPO',
            grupoAnimal: null,
            numeroApostado: null,
            segundoNumero: null,
            valor: null
          });
          this.aplicarValidadoresPorTipo('GRUPO');

          this.cdr.detectChanges();
          this.atualizarUsuarioEHistorico();
        },

        error: error => {
          console.error('Erro ao apostar:', error);

          this.erro = this.extrairMensagemErro(
            error,
            'Não foi possível registrar aposta.'
          );

          this.cdr.detectChanges();
        }
      });
  }

  depositar(): void {

    if (this.depositoForm.invalid || !this.usuarioLogado?.usuarioId) {
      this.depositoForm.markAllAsTouched();
      return;
    }

    const valor = Number(this.depositoForm.value.valorDeposito);

    this.carregandoDeposito = true;
    this.erro = '';
    this.sucesso = '';

    this.usuarioService.depositar(this.usuarioLogado.usuarioId, { valor })
      .pipe(
        timeout(8000),
        finalize(() => {
          this.carregandoDeposito = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: usuarioAtualizado => {
          this.usuarioAtual = usuarioAtualizado;

          this.sucesso = `Depósito de R$ ${this.formatarMoeda(valor)} realizado com sucesso.`;

          this.depositoForm.reset({
            valorDeposito: null
          });

          this.cdr.detectChanges();
        },

        error: error => {
          console.error('Erro ao depositar:', error);

          this.erro = this.extrairMensagemErro(
            error,
            'Não foi possível realizar o depósito.'
          );

          this.cdr.detectChanges();
        }
      });
  }

  atualizarUsuarioEHistorico(): void {

    if (!this.usuarioLogado?.usuarioId) {
      return;
    }

    const usuarioId = this.usuarioLogado.usuarioId;

    this.carregandoHistorico = true;
    this.cdr.detectChanges();

    this.usuarioService.buscarPorId(usuarioId)
      .pipe(
        timeout(8000),
        catchError(() => of(null))
      )
      .subscribe(usuario => {
        if (usuario) {
          this.usuarioAtual = usuario;
        }

        this.cdr.detectChanges();
      });

    this.apostaService.listarHistorico(usuarioId)
      .pipe(
        timeout(8000),
        catchError(() => of([])),
        finalize(() => {
          this.carregandoHistorico = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe(historico => {
        this.historico = historico;
        this.cdr.detectChanges();
      });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  alterarTipoAposta(): void {
    const tipo = this.form.value.tipoAposta || 'GRUPO';
    this.aplicarValidadoresPorTipo(tipo);
  }

  private aplicarValidadoresPorTipo(tipo: string): void {
    const grupoAnimal = this.form.get('grupoAnimal');
    const numeroApostado = this.form.get('numeroApostado');
    const segundoNumero = this.form.get('segundoNumero');

    grupoAnimal?.clearValidators();
    numeroApostado?.clearValidators();
    segundoNumero?.clearValidators();

    if (tipo === 'GRUPO') {
      grupoAnimal?.setValidators([Validators.required]);
      numeroApostado?.setValue(null);
      segundoNumero?.setValue(null);
    } else if (tipo === 'DUQUE_DE_DEZENA') {
      numeroApostado?.setValidators([Validators.required, Validators.pattern(/^\d{1,2}$/)]);
      segundoNumero?.setValidators([Validators.required, Validators.pattern(/^\d{1,2}$/)]);
      grupoAnimal?.setValue(null);
    } else if (tipo === 'DEZENA') {
      numeroApostado?.setValidators([Validators.required, Validators.pattern(/^\d{1,2}$/)]);
      grupoAnimal?.setValue(null);
      segundoNumero?.setValue(null);
    } else if (tipo === 'CENTENA') {
      numeroApostado?.setValidators([Validators.required, Validators.pattern(/^\d{1,3}$/)]);
      grupoAnimal?.setValue(null);
      segundoNumero?.setValue(null);
    } else if (tipo === 'MILHAR') {
      numeroApostado?.setValidators([Validators.required, Validators.pattern(/^\d{1,4}$/)]);
      grupoAnimal?.setValue(null);
      segundoNumero?.setValue(null);
    }

    grupoAnimal?.updateValueAndValidity();
    numeroApostado?.updateValueAndValidity();
    segundoNumero?.updateValueAndValidity();
  }

  private validarCamposAposta(tipo: string): boolean {
    this.aplicarValidadoresPorTipo(tipo);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return false;
    }

    return true;
  }

  tipoApostaSelecionado(): string {
    return this.form.value.tipoAposta || 'GRUPO';
  }

  obterRotuloTipo(tipo: string | null | undefined): string {
    return this.tiposAposta.find(item => item.valor === tipo)?.rotulo ?? 'Grupo';
  }

  obterRotuloCampoNumero(): string {
    const tipo = this.tipoApostaSelecionado();

    if (tipo === 'DEZENA' || tipo === 'DUQUE_DE_DEZENA') {
      return 'Primeira dezena';
    }

    if (tipo === 'CENTENA') {
      return 'Centena';
    }

    if (tipo === 'MILHAR') {
      return 'Milhar';
    }

    return 'Número';
  }

  obterPlaceholderNumero(): string {
    const tipo = this.tipoApostaSelecionado();

    if (tipo === 'DEZENA' || tipo === 'DUQUE_DE_DEZENA') {
      return 'Ex.: 07 ou 45';
    }

    if (tipo === 'CENTENA') {
      return 'Ex.: 123';
    }

    if (tipo === 'MILHAR') {
      return 'Ex.: 1234';
    }

    return '';
  }

  obterTextoAposta(item: ApostaHistorico): string {
    const tipo = item.tipoAposta || 'GRUPO';

    if (tipo === 'GRUPO') {
      return `Grupo ${item.grupoAnimal} - ${item.nomeAnimal}`;
    }

    if (tipo === 'DUQUE_DE_DEZENA') {
      return `${this.obterRotuloTipo(tipo)}: ${item.numeroApostado} e ${item.segundoNumero}`;
    }

    return `${this.obterRotuloTipo(tipo)}: ${item.numeroApostado}`;
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
    const payload = error?.error;

    if (typeof payload === 'string') {
      return payload;
    }

    if (payload?.message) {
      return payload.message;
    }

    if (payload?.erro) {
      return payload.erro;
    }

    if (payload?.error) {
      return payload.error;
    }

    if (payload && typeof payload === 'object') {
      return JSON.stringify(payload);
    }

    return fallback;
  }

  get tipoAposta() {
    return this.form.get('tipoAposta');
  }

  get grupoAnimal() {
    return this.form.get('grupoAnimal');
  }

  get numeroApostado() {
    return this.form.get('numeroApostado');
  }

  get segundoNumero() {
    return this.form.get('segundoNumero');
  }

  get valor() {
    return this.form.get('valor');
  }

  get valorDeposito() {
    return this.depositoForm.get('valorDeposito');
  }
}