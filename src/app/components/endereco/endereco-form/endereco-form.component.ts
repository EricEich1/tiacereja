import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { EnderecoService } from '../../../services/endereco.service';
import { Endereco, EnderecoRequest } from '../../../models/endereco.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-endereco-form',
  imports: [CommonModule, FormsModule],
  templateUrl: './endereco-form.component.html',
  styleUrl: './endereco-form.component.scss'
})
export class EnderecoFormComponent implements OnInit {
  endereco: EnderecoRequest = {
    logradouro: '',
    numero: '',
    complemento: '',
    bairro: '',
    cidade: '',
    estado: '',
    cep: '',
    clienteId: undefined
  };

  isEdit = false;
  loading = false;
  enderecoId?: number;

  constructor(
    private enderecoService: EnderecoService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.enderecoId = this.route.snapshot.params['id'];
    this.isEdit = !!this.enderecoId;
    
    if (this.isEdit && this.enderecoId) {
      this.carregarEndereco();
    }
  }

  carregarEndereco() {
    this.loading = true;
    this.enderecoService.buscarPorId(this.enderecoId!).subscribe({
      next: (endereco) => {
        this.endereco = {
          logradouro: endereco.logradouro,
          numero: endereco.numero,
          complemento: endereco.complemento || '',
          bairro: endereco.bairro,
          cidade: endereco.cidade,
          estado: endereco.estado,
          cep: endereco.cep,
          clienteId: endereco.clienteId
        };
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar endereço:', error);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Erro ao carregar endereço',
          text: 'Não foi possível carregar os dados do endereço.'
        });
      }
    });
  }

  salvar() {
    if (!this.validarFormulario()) {
      return;
    }

    this.loading = true;

    if (this.isEdit && this.enderecoId) {
      this.enderecoService.atualizar(this.enderecoId, this.endereco).subscribe({
        next: () => {
          this.loading = false;
          Swal.fire({
            icon: 'success',
            title: 'Sucesso!',
            text: 'Endereço atualizado com sucesso!',
            timer: 1500,
            showConfirmButton: false
          }).then(() => {
            this.router.navigate(['/enderecos']);
          });
        },
        error: (error) => {
          this.loading = false;
          console.error('Erro ao atualizar endereço:', error);
          Swal.fire({
            icon: 'error',
            title: 'Erro ao atualizar endereço',
            text: 'Não foi possível atualizar o endereço. Tente novamente.'
          });
        }
      });
    } else {
      this.enderecoService.salvar(this.endereco).subscribe({
        next: () => {
          this.loading = false;
          Swal.fire({
            icon: 'success',
            title: 'Sucesso!',
            text: 'Endereço cadastrado com sucesso!',
            timer: 1500,
            showConfirmButton: false
          }).then(() => {
            this.router.navigate(['/enderecos']);
          });
        },
        error: (error) => {
          this.loading = false;
          console.error('Erro ao salvar endereço:', error);
          Swal.fire({
            icon: 'error',
            title: 'Erro ao salvar endereço',
            text: 'Não foi possível salvar o endereço. Tente novamente.'
          });
        }
      });
    }
  }

  validarFormulario(): boolean {
    if (!this.endereco.logradouro.trim()) {
      Swal.fire('Atenção!', 'Logradouro é obrigatório.', 'warning');
      return false;
    }

    if (!this.endereco.numero.trim()) {
      Swal.fire('Atenção!', 'Número é obrigatório.', 'warning');
      return false;
    }

    if (!this.endereco.bairro.trim()) {
      Swal.fire('Atenção!', 'Bairro é obrigatório.', 'warning');
      return false;
    }

    if (!this.endereco.cidade.trim()) {
      Swal.fire('Atenção!', 'Cidade é obrigatória.', 'warning');
      return false;
    }

    if (!this.endereco.estado.trim()) {
      Swal.fire('Atenção!', 'Estado é obrigatório.', 'warning');
      return false;
    }

    if (!this.endereco.cep.trim()) {
      Swal.fire('Atenção!', 'CEP é obrigatório.', 'warning');
      return false;
    }

    return true;
  }

  cancelar() {
    this.router.navigate(['/enderecos']);
  }
}
