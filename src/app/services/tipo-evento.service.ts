import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoEvento } from '../models/tipo-evento.model';
import { API_CONFIG } from '../shared/api.config';

@Injectable({
  providedIn: 'root'
})
export class TipoEventoService {
  private readonly baseUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.tiposEvento}`;

  constructor(private http: HttpClient) { }

  // Buscar todos os tipos de evento
  buscarTodos(): Observable<TipoEvento[]> {
    return this.http.get<TipoEvento[]>(this.baseUrl);
  }

  // Buscar tipo de evento por ID
  buscarPorId(id: number): Observable<TipoEvento> {
    return this.http.get<TipoEvento>(`${this.baseUrl}/${id}`);
  }

  // Salvar novo tipo de evento
  salvar(tipoEvento: TipoEvento): Observable<TipoEvento> {
    return this.http.post<TipoEvento>(this.baseUrl, tipoEvento);
  }

  // Atualizar tipo de evento
  atualizar(id: number, tipoEvento: TipoEvento): Observable<TipoEvento> {
    return this.http.put<TipoEvento>(`${this.baseUrl}/${id}`, tipoEvento);
  }

  // Deletar tipo de evento
  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Buscar tipos de evento por nome
  buscarPorNome(nome: string): Observable<TipoEvento[]> {
    const params = new HttpParams().set('nome', nome);
    return this.http.get<TipoEvento[]>(`${this.baseUrl}/buscar`, { params });
  }

  // Buscar tipos de evento por capacidade
  buscarPorCapacidade(capacidade: number): Observable<TipoEvento[]> {
    return this.http.get<TipoEvento[]>(`${this.baseUrl}/capacidade/${capacidade}`);
  }
}
