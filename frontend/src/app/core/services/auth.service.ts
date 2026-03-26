import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { LoginRequest } from '../../shared/models/login-request.model';
import { LoginResponse } from '../../shared/models/login-response.model';
import { UsuarioRequest } from '../../shared/models/usuario-request.model';
import { UsuarioResponse } from '../../shared/models/usuario-response.model';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly storageService = inject(StorageService);

  private readonly apiUrl = 'http://localhost:8080';

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, payload).pipe(
      tap((response) => {
        this.storageService.setToken(response.token);
        this.storageService.setUser(response);
      })
    );
  }

  cadastrar(payload: UsuarioRequest): Observable<UsuarioResponse> {
    return this.http.post<UsuarioResponse>(`${this.apiUrl}/usuarios`, payload);
  }

  logout(): void {
    this.storageService.clear();
  }

  getToken(): string | null {
    return this.storageService.getToken();
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getUsuarioLogado(): LoginResponse | null {
    return this.storageService.getUser<LoginResponse>();
  }
}