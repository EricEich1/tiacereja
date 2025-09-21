import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginRequest, LoginResponse, Usuario } from '../models/usuario.model';
import { API_CONFIG } from '../shared/api.config';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly baseUrl = `${API_CONFIG.baseUrl}/api/auth`;
  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

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

  // Login
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, credentials)
      .pipe(
        tap(response => {
          if (response.usuario) {
            this.currentUserSubject.next(response.usuario);
            localStorage.setItem('currentUser', JSON.stringify(response.usuario));
            if (response.token) {
              localStorage.setItem('token', response.token);
            }
          }
        })
      );
  }

  // Logout
  logout(): void {
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
