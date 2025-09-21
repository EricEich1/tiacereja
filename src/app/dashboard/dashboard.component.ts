import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  stats = {
    clientes: 0,
    solicitacoes: 0,
    temas: 0,
    tiposEvento: 0
  };

  ngOnInit() {
    // Simular carregamento de estatísticas
    this.loadStats();
  }

  loadStats() {
    // Por enquanto, valores simulados
    // Aqui você integraria com os services para buscar dados reais
    this.stats = {
      clientes: 25,
      solicitacoes: 12,
      temas: 8,
      tiposEvento: 5
    };
  }
}
