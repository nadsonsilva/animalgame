import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Animal } from '../../shared/models/animal.model';

@Injectable({
  providedIn: 'root'
})
export class AnimalService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080';

  listarAnimais(): Observable<Animal[]> {
    return this.http.get<Animal[]>(`${this.apiUrl}/animais`);
  }
}