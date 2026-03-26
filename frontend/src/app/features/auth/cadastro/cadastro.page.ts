import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-cadastro-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cadastro.page.html',
  styleUrl: './cadastro.page.css'
})
export class CadastroPage {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  loading = false;
  sucesso = '';
  erro = '';

  form = this.fb.group({
    nome: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    senha: ['', [Validators.required, Validators.minLength(4)]],
    saldo: [0, [Validators.required, Validators.min(0)]]
  });

  cadastrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.erro = '';
    this.sucesso = '';

    const payload = {
      nome: this.form.value.nome ?? '',
      email: this.form.value.email ?? '',
      senha: this.form.value.senha ?? '',
      saldo: Number(this.form.value.saldo ?? 0)
    };

    this.authService.cadastrar(payload).subscribe({
      next: () => {
        this.loading = false;
        this.sucesso = 'Cadastro realizado com sucesso.';
        this.form.reset({
          nome: '',
          email: '',
          senha: '',
          saldo: 0
        });

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1200);
      },
      error: (error) => {
        this.loading = false;
        this.erro = error?.error?.message || error?.error || 'Não foi possível realizar o cadastro.';
      }
    });
  }

  voltarParaLogin(): void {
    this.router.navigate(['/login']);
  }

  get nome() {
    return this.form.get('nome');
  }

  get email() {
    return this.form.get('email');
  }

  get senha() {
    return this.form.get('senha');
  }

  get saldo() {
    return this.form.get('saldo');
  }
}