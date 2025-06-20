import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UsuarioResponse, UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-admin-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './admin-clientes.component.html',
  styleUrl: './admin-clientes.component.css',
})
export class AdminClientesComponent implements OnInit {
  clientes: UsuarioResponse[] = [];
  clienteSeleccionado: UsuarioResponse | null = null;
  formData: Partial<UsuarioResponse> = {};
  mensaje: string = '';
  mensajeType: 'success' | 'error' | 'info' = 'info';
  isSubmitting = false;
  formSubmitted = false;
  showDeleteModal = false;
  clienteToDelete: number | null = null;
  showCannotDeleteModal = false;
  clienteWithPets: number | null = null;

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.usuarioService.getClientes().subscribe({
      next: (data) => {
        this.clientes = data;
      },
      error: () => {
        this.showAlert('Error al cargar clientes', 'error');
      },
    });
  }

  seleccionarCliente(cliente: UsuarioResponse): void {
    this.clienteSeleccionado = cliente;
    this.formData = { ...cliente };
    this.formSubmitted = false;
  }

  actualizarCliente(): void {
    this.formSubmitted = true;
    this.isSubmitting = true;

    if (
      !this.formData.nombre ||
      !this.formData.apellido ||
      !this.formData.direccion
    ) {
      this.isSubmitting = false;
      return;
    }

    if (!this.clienteSeleccionado) {
      this.isSubmitting = false;
      return;
    }

    this.usuarioService
      .updateUsuario(this.clienteSeleccionado.id, this.formData)
      .subscribe({
        next: () => {
          this.showAlert('Cliente actualizado correctamente', 'success');
          this.cargarClientes();
          this.cancelarEdicion();
          this.isSubmitting = false;
        },
        error: (err) => {
          const errorMsg = err.error?.message || 'Error al actualizar cliente';
          this.showAlert(errorMsg, 'error');
          this.isSubmitting = false;
        },
      });
  }

  confirmarEliminacion(id: number): void {
    this.clienteToDelete = id;
    this.showDeleteModal = true;
  }

  eliminarCliente(): void {
    if (!this.clienteToDelete) return;

    this.usuarioService.deleteUsuario(this.clienteToDelete).subscribe({
      next: () => {
        this.showAlert('Cliente eliminado correctamente', 'success');
        this.cargarClientes();
        this.showDeleteModal = false;
      },
      error: (err) => {
        // Verificar si es error de clave foránea
        if (
          err.error?.message?.includes('foreign key constraint fails') ||
          err.message?.includes('foreign key constraint fails')
        ) {
          this.clienteWithPets = this.clienteToDelete;
          this.showDeleteModal = false;
          this.showCannotDeleteModal = true;
        } else {
          const errorMsg = err.error?.message || 'Error al eliminar cliente';
          this.showAlert(errorMsg, 'error');
        }
        this.showDeleteModal = false;
      },
    });
  }

  transferirMascotas(): void {
    if (!this.clienteWithPets) return;

    // Aquí implementarías la lógica para transferir mascotas a otro cliente
    // Por ejemplo:
    // this.usuarioService.transferPetsToAnotherOwner(this.clienteWithPets, newOwnerId)
    //   .subscribe(...);

    // Por ahora solo cerramos el modal
    this.showCannotDeleteModal = false;
    this.showAlert('Funcionalidad de transferencia en desarrollo', 'info');
  }

  cancelarEdicion(): void {
    this.clienteSeleccionado = null;
    this.formData = {};
    this.formSubmitted = false;
  }

  showAlert(message: string, type: 'success' | 'error' | 'info'): void {
    this.mensaje = message;
    this.mensajeType = type;

    setTimeout(() => {
      this.dismissAlert();
    }, 5000);
  }

  dismissAlert(): void {
    this.mensaje = '';
  }
}
