import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SolicitacaoOrcamento } from '../models/solicitacao-orcamento.model';
import { API_CONFIG } from '../shared/api.config';

@Injectable({
  providedIn: 'root'
})
export class SolicitacaoOrcamentoService {
  private readonly baseUrl = `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.solicitacoes}`;

  constructor(private http: HttpClient) { }

  // Buscar todas as solicitações
  buscarTodos(): Observable<SolicitacaoOrcamento[]> {
    return this.http.get<SolicitacaoOrcamento[]>(this.baseUrl);
  }

  // Buscar solicitação por ID
  buscarPorId(id: number): Observable<SolicitacaoOrcamento> {
    return this.http.get<SolicitacaoOrcamento>(`${this.baseUrl}/${id}`);
  }

  // Salvar nova solicitação
  salvar(solicitacao: SolicitacaoOrcamento): Observable<SolicitacaoOrcamento> {
    return this.http.post<SolicitacaoOrcamento>(this.baseUrl, solicitacao);
  }

  // Atualizar solicitação
  atualizar(id: number, solicitacao: SolicitacaoOrcamento): Observable<SolicitacaoOrcamento> {
    return this.http.put<SolicitacaoOrcamento>(`${this.baseUrl}/${id}`, solicitacao);
  }

  // Deletar solicitação
  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Buscar solicitações por status
  buscarPorStatus(status: string): Observable<SolicitacaoOrcamento[]> {
    return this.http.get<SolicitacaoOrcamento[]>(`${this.baseUrl}/status/${status}`);
  }

  // Buscar solicitações por cliente
  buscarPorCliente(clienteId: number): Observable<SolicitacaoOrcamento[]> {
    return this.http.get<SolicitacaoOrcamento[]>(`${this.baseUrl}/cliente/${clienteId}`);
  }
}
