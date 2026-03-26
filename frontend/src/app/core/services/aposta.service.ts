import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApostaRequest } from '../../shared/models/aposta-request.model';
import { ApostaResponse } from '../../shared/models/aposta-response.model';
import { ApostaHistorico } from '../../shared/models/aposta-historico.model';

@Injectable({
  providedIn: 'root'
})
export class ApostaService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080';

  registrarAposta(payload: ApostaRequest): Observable<ApostaResponse> {
    return this.http.post<ApostaResponse>(`${this.apiUrl}/apostas`, payload);
  }

  listarHistorico(usuarioId: number): Observable<ApostaHistorico[]> {
    return this.http.get<ApostaHistorico[]>(`${this.apiUrl}/apostas/historico/${usuarioId}`);
  }
}