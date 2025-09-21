import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TemaFesta } from '../models/tema-festa.model';
import { API_CONFIG } from '../shared/api.config';

@Injectable({
  providedIn: 'root'
})
export class TemaFestaService {
  private readonly baseUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.temas}`;

  constructor(private http: HttpClient) { }

  // Buscar todos os temas
  buscarTodos(): Observable<TemaFesta[]> {
    return this.http.get<TemaFesta[]>(this.baseUrl);
  }

  // Buscar tema por ID
  buscarPorId(id: number): Observable<TemaFesta> {
    return this.http.get<TemaFesta>(`${this.baseUrl}/${id}`);
  }

  // Salvar novo tema
  salvar(tema: TemaFesta): Observable<TemaFesta> {
    return this.http.post<TemaFesta>(this.baseUrl, tema);
  }

  // Atualizar tema
  atualizar(id: number, tema: TemaFesta): Observable<TemaFesta> {
    return this.http.put<TemaFesta>(`${this.baseUrl}/${id}`, tema);
  }

  // Deletar tema
  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Buscar temas por nome
  buscarPorNome(nome: string): Observable<TemaFesta[]> {
    const params = new HttpParams().set('nome', nome);
    return this.http.get<TemaFesta[]>(`${this.baseUrl}/buscar`, { params });
  }

  // Buscar temas ativos
  buscarAtivos(): Observable<TemaFesta[]> {
    return this.http.get<TemaFesta[]>(`${this.baseUrl}/ativos`);
  }
}
