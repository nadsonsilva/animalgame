import { Routes } from '@angular/router';

import { LoginPage } from './features/auth/login/login.page';
import { CadastroPage } from './features/auth/cadastro/cadastro.page';
import { DashboardPage } from './features/dashboard/dashboard.page';

import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginPage
  },
  {
    path: 'cadastro',
    component: CadastroPage
  },
  {
    path: 'dashboard',
    component: DashboardPage,
    canActivate: [authGuard]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];