import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Endereco } from '../models/cliente.model';
import { API_CONFIG } from '../shared/api.config';

@Injectable({
  providedIn: 'root'
})
export class EnderecoService {
  private readonly baseUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.enderecos}`;

  constructor(private http: HttpClient) { }

  // Buscar todos os endereços
  buscarTodos(): Observable<Endereco[]> {
    return this.http.get<Endereco[]>(this.baseUrl);
  }

  // Buscar endereço por ID
  buscarPorId(id: number): Observable<Endereco> {
    return this.http.get<Endereco>(`${this.baseUrl}/${id}`);
  }

  // Salvar novo endereço
  salvar(endereco: Endereco): Observable<Endereco> {
    return this.http.post<Endereco>(this.baseUrl, endereco);
  }

  // Atualizar endereço
  atualizar(id: number, endereco: Endereco): Observable<Endereco> {
    return this.http.put<Endereco>(`${this.baseUrl}/${id}`, endereco);
  }

  // Deletar endereço
  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Buscar endereços por cidade
  buscarPorCidade(cidade: string): Observable<Endereco[]> {
    const params = new HttpParams().set('cidade', cidade);
    return this.http.get<Endereco[]>(`${this.baseUrl}/cidade`, { params });
  }

  // Buscar endereços por estado
  buscarPorEstado(estado: string): Observable<Endereco[]> {
    return this.http.get<Endereco[]>(`${this.baseUrl}/estado/${estado}`);
  }
}
