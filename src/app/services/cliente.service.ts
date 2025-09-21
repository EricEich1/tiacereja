import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';
import { API_CONFIG } from '../shared/api.config';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private readonly baseUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.clientes}`;

  constructor(private http: HttpClient) { }

  // Buscar todos os clientes
  buscarTodos(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.baseUrl);
  }

  // Buscar cliente por ID
  buscarPorId(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.baseUrl}/${id}`);
  }

  // Salvar novo cliente
  salvar(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.baseUrl, cliente);
  }

  // Atualizar cliente
  atualizar(id: number, cliente: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.baseUrl}/${id}`, cliente);
  }

  // Deletar cliente
  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Buscar clientes por nome
  buscarPorNome(nome: string): Observable<Cliente[]> {
    const params = new HttpParams().set('nome', nome);
    return this.http.get<Cliente[]>(`${this.baseUrl}/buscar`, { params });
  }

  // Buscar cliente por telefone
  buscarPorTelefone(telefone: string): Observable<Cliente[]> {
    const params = new HttpParams().set('telefone', telefone);
    return this.http.get<Cliente[]>(`${this.baseUrl}/telefone`, { params });
  }

  // Buscar clientes por status
  buscarPorStatus(status: string): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.baseUrl}/status/${status}`);
  }
}
