import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    // Verificar se há usuário no localStorage
    const savedUser = localStorage.getItem('currentUser');
    
    if (savedUser) {
      try {
        const user = JSON.parse(savedUser);
        // Não precisamos chamar next aqui, apenas verificar se existe
        return true;
      } catch (error) {
        localStorage.removeItem('currentUser');
        localStorage.removeItem('token');
        this.router.navigate(['/login']);
        return false;
      }
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
