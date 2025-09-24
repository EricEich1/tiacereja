import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, of, throwError } from 'rxjs';
import { tap, delay } from 'rxjs/operators';
import { LoginRequest, LoginResponse, Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  // Usuários mockados para demonstração
  private mockUsers: Usuario[] = [
    {
      id: 1,
      email: 'admin@tiacereja.com',
      senha: '123456',
      nome: 'Administrador',
      role: 'ADMIN'
    },
    {
      id: 2,
      email: 'user@tiacereja.com',
      senha: '123456',
      nome: 'Usuário',
      role: 'USER'
    },
    {
      id: 3,
      email: 'demo@tiacereja.com',
      senha: 'demo123',
      nome: 'Demo User',
      role: 'USER'
    }
  ];

  constructor(private http: HttpClient) {
    // Verificar se há usuário salvo no localStorage
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      try {
        this.currentUserSubject.next(JSON.parse(savedUser));
      } catch (error) {
        // Se não for um JSON válido, limpar o localStorage
        localStorage.removeItem('currentUser');
        localStorage.removeItem('token');
      }
    }
  }

  // Login local sem backend
  login(credentials: LoginRequest): Observable<LoginResponse> {
    const user = this.mockUsers.find(u => 
      u.email === credentials.email && u.senha === credentials.senha
    );

    if (user) {
      const response: LoginResponse = {
        usuario: { ...user },
        token: this.generateMockToken(),
        message: 'Login realizado com sucesso'
      };

      this.currentUserSubject.next(response.usuario);
      localStorage.setItem('currentUser', JSON.stringify(response.usuario));
      if (response.token) {
        localStorage.setItem('token', response.token);
      }
      
      return of(response).pipe(delay(1000)); // Simular delay de rede
    } else {
      return throwError(() => new Error('Credenciais inválidas'));
    }
  }

  private generateMockToken(): string {
    return 'mock_token_' + Math.random().toString(36).substr(2, 9);
  }

  // Logout
  logout(): void {
    this.currentUserSubject.next(null);
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
  }

  // Limpar dados de autenticação
  clearAuthData(): void {
    this.currentUserSubject.next(null);
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
  }

  // Verificar se está logado
  isLoggedIn(): boolean {
    return this.currentUserSubject.value !== null;
  }

  // Obter usuário atual
  getCurrentUser(): Usuario | null {
    return this.currentUserSubject.value;
  }

  // Obter token
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Verificar se é admin
  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user?.role === 'ADMIN';
  }
}
