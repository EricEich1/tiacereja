import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Usuario } from '../../models/usuario.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit {
  currentUser: Usuario | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  getInitials(): string {
    if (!this.currentUser) return 'U';
    
    const name = this.currentUser.nome || this.currentUser.email;
    return name
      .split(' ')
      .map(word => word.charAt(0))
      .join('')
      .toUpperCase()
      .substring(0, 2);
  }


  logout(event?: Event): void {
    // Prevenir comportamento padrão do link
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }
    
    Swal.fire({
      title: 'Tem certeza?',
      text: 'Você será deslogado do sistema.',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sim, sair!',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        // Primeiro limpa os dados
        this.authService.logout();
        
        // Depois redireciona para login
        this.router.navigate(['/login']).then(() => {
          // Por último mostra a mensagem de sucesso
          Swal.fire({
            icon: 'success',
            title: 'Logout realizado!',
            text: 'Você foi deslogado com sucesso.',
            timer: 1500,
            showConfirmButton: false
          });
        });
      }
    });
  }
}
