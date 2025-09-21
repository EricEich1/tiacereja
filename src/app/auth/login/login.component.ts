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
        
        let mensagemErro = 'Erro no login';
        
        if (error.status === 0) {
          mensagemErro = 'Não foi possível conectar ao servidor. Verifique se o backend está rodando.';
        } else if (error.status === 401) {
          mensagemErro = 'Email ou senha incorretos!';
        } else if (error.status === 404) {
          mensagemErro = 'Endpoint de login não encontrado. Verifique a configuração da API.';
        } else if (error.error && error.error.message) {
          mensagemErro = error.error.message;
        }
        
        Swal.fire({
          icon: 'error',
          title: 'Erro no login',
          text: mensagemErro,
          footer: `Status: ${error.status || 'N/A'}`
        });
      }
    });
  }
}
