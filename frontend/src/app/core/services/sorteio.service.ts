import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SorteioResponse } from '../../shared/models/sorteio-response.model';

@Injectable({
  providedIn: 'root'
})
export class SorteioService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080';

  simularSorteio(usuarioId?: number): Observable<SorteioResponse> {
    const url = usuarioId
      ? `${this.apiUrl}/sorteio/simular?usuarioId=${usuarioId}`
      : `${this.apiUrl}/sorteio/simular`;

    return this.http.get<SorteioResponse>(url);
  }
}
