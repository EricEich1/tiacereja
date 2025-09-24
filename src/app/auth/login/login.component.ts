import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/usuario.model';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  credentials: LoginRequest = {
    email: '',
    senha: ''
  };
  
  loading = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  login() {
    if (!this.credentials.email || !this.credentials.senha) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos obrigatórios',
        text: 'Email e senha são obrigatórios!'
      });
      return;
    }

    this.loading = true;
    
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        this.loading = false;
        console.log('Login realizado com sucesso:', response);
        
        Swal.fire({
          icon: 'success',
          title: 'Login realizado!',
          text: `Bem-vindo, ${response.usuario.nome || response.usuario.email}!`,
          timer: 1500,
          showConfirmButton: false
        }).then(() => {
          this.router.navigate(['/dashboard']);
        });
      },
      error: (error) => {
        this.loading = false;
        console.error('Erro no login:', error);
        
        let mensagemErro = 'Email ou senha incorretos!';
        
        if (error.message) {
          mensagemErro = error.message;
        }
        
        Swal.fire({
          icon: 'error',
          title: 'Erro no login',
          text: mensagemErro
        });
      }
    });
  }

  clearStorage() {
    this.authService.clearAuthData();
    Swal.fire({
      icon: 'success',
      title: 'Dados limpos!',
      text: 'LocalStorage foi limpo com sucesso.',
      timer: 1500,
      showConfirmButton: false
    });
  }
}
