import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TemaFesta, TemaFestaRequest } from '../models/tema-festa.model';
import { API_CONFIG } from '../shared/api.config';

@Injectable({
  providedIn: 'root'
})
export class TemaFestaService {
  private readonly baseUrl = `${API_CONFIG.baseUrl}/api/temas`;

  constructor(private http: HttpClient) { }

  // Buscar todos os temas
  buscarTodos(): Observable<TemaFesta[]> {
    return this.http.get<TemaFesta[]>(this.baseUrl);
  }

  // Buscar tema por ID
  buscarPorId(id: number): Observable<TemaFesta> {
    return this.http.get<TemaFesta>(`${this.baseUrl}/${id}`);
  }

  // Salvar tema
  salvar(tema: TemaFestaRequest): Observable<TemaFesta> {
    return this.http.post<TemaFesta>(this.baseUrl, tema);
  }

  // Atualizar tema
  atualizar(id: number, tema: TemaFestaRequest): Observable<TemaFesta> {
    return this.http.put<TemaFesta>(`${this.baseUrl}/${id}`, tema);
  }

  // Excluir tema
  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Buscar temas
  buscar(nome?: string): Observable<TemaFesta[]> {
    const params = nome ? `?nome=${nome}` : '';
    return this.http.get<TemaFesta[]>(`${this.baseUrl}/buscar${params}`);
  }

  // Buscar temas ativos
  buscarAtivos(): Observable<TemaFesta[]> {
    return this.http.get<TemaFesta[]>(`${this.baseUrl}/ativos`);
  }
}