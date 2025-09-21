import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoEvento, TipoEventoRequest } from '../models/tipo-evento.model';
import { API_CONFIG } from '../shared/api.config';

@Injectable({
  providedIn: 'root'
})
export class TipoEventoService {
  private readonly baseUrl = `${API_CONFIG.baseUrl}/api/tipos-evento`;

  constructor(private http: HttpClient) { }

  // Buscar todos os tipos de evento
  buscarTodos(): Observable<TipoEvento[]> {
    return this.http.get<TipoEvento[]>(this.baseUrl);
  }

  // Buscar tipo de evento por ID
  buscarPorId(id: number): Observable<TipoEvento> {
    return this.http.get<TipoEvento>(`${this.baseUrl}/${id}`);
  }

  // Salvar tipo de evento
  salvar(tipoEvento: TipoEventoRequest): Observable<TipoEvento> {
    return this.http.post<TipoEvento>(this.baseUrl, tipoEvento);
  }

  // Atualizar tipo de evento
  atualizar(id: number, tipoEvento: TipoEventoRequest): Observable<TipoEvento> {
    return this.http.put<TipoEvento>(`${this.baseUrl}/${id}`, tipoEvento);
  }

  // Excluir tipo de evento
  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Buscar tipos de evento
  buscar(nome?: string): Observable<TipoEvento[]> {
    const params = nome ? `?nome=${nome}` : '';
    return this.http.get<TipoEvento[]>(`${this.baseUrl}/buscar${params}`);
  }

  // Buscar por capacidade
  buscarPorCapacidade(capacidade: number): Observable<TipoEvento[]> {
    return this.http.get<TipoEvento[]>(`${this.baseUrl}/capacidade/${capacidade}`);
  }
}