import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Endereco, EnderecoRequest } from '../models/endereco.model';
import { API_CONFIG } from '../shared/api.config';

@Injectable({
  providedIn: 'root'
})
export class EnderecoService {
  private readonly baseUrl = `${API_CONFIG.baseUrl}/api/enderecos`;

  constructor(private http: HttpClient) { }

  // Buscar todos os endereços
  buscarTodos(): Observable<Endereco[]> {
    return this.http.get<Endereco[]>(this.baseUrl);
  }

  // Buscar endereço por ID
  buscarPorId(id: number): Observable<Endereco> {
    return this.http.get<Endereco>(`${this.baseUrl}/${id}`);
  }

  // Salvar endereço
  salvar(endereco: EnderecoRequest): Observable<Endereco> {
    return this.http.post<Endereco>(this.baseUrl, endereco);
  }

  // Atualizar endereço
  atualizar(id: number, endereco: EnderecoRequest): Observable<Endereco> {
    return this.http.put<Endereco>(`${this.baseUrl}/${id}`, endereco);
  }

  // Excluir endereço
  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Buscar por cidade
  buscarPorCidade(cidade: string): Observable<Endereco[]> {
    return this.http.get<Endereco[]>(`${this.baseUrl}/cidade?cidade=${cidade}`);
  }

  // Buscar por estado
  buscarPorEstado(estado: string): Observable<Endereco[]> {
    return this.http.get<Endereco[]>(`${this.baseUrl}/estado/${estado}`);
  }
}