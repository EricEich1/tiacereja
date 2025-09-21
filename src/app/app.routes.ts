import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { LayoutComponent } from './shared/layout/layout.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'clientes',
        loadChildren: () => import('./components/cliente/cliente.routes').then(m => m.clienteRoutes)
      }
      // TODO: Adicionar outras rotas quando os componentes estiverem prontos
      // {
      //   path: 'solicitacoes',
      //   loadChildren: () => import('./components/solicitacao/solicitacao.routes').then(m => m.solicitacaoRoutes)
      // },
      // {
      //   path: 'temas',
      //   loadChildren: () => import('./components/tema/tema.routes').then(m => m.temaRoutes)
      // },
      // {
      //   path: 'tipos-evento',
      //   loadChildren: () => import('./components/tipo-evento/tipo-evento.routes').then(m => m.tipoEventoRoutes)
      // },
      // {
      //   path: 'enderecos',
      //   loadChildren: () => import('./components/endereco/endereco.routes').then(m => m.enderecoRoutes)
      // }
    ]
  },
  {
    path: '**',
    redirectTo: '/login'
  }
];
