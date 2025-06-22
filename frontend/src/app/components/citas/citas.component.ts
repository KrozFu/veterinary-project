import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CitaResponse, CitaService } from '../../services/cita.service';

@Component({
  selector: 'app-citas',
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './citas.component.html',
  styleUrl: './citas.component.css',
})
export class CitasComponent implements OnInit {
  citas: CitaResponse[] = [];
  rolUsuario: string = '';
  formCita: any = {};
  editarCitaId: number | null = null;
  mensaje = '';

  constructor(private citaService: CitaService) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (!token) return;

    const payload = JSON.parse(atob(token.split('.')[1]));
    this.rolUsuario = payload.roles[0]?.replace('ROLE_', '') ?? '';

    this.cargarCitas();
  }

  cargarCitas(): void {
    this.citaService.getCitasPorRol(this.rolUsuario).subscribe({
      next: (data) => (this.citas = data),
      error: () => (this.mensaje = 'Error al cargar citas'),
    });
  }

  guardarCita(): void {
    if (this.editarCitaId) {
      this.citaService
        .actualizarCita(this.editarCitaId, this.formCita)
        .subscribe({
          next: () => {
            this.mensaje = 'Cita actualizada';
            this.cargarCitas();
            this.cancelar();
          },
        });
    } else {
      this.citaService.crearCita(this.formCita).subscribe({
        next: () => {
          this.mensaje = 'Cita creada';
          this.cargarCitas();
          this.cancelar();
        },
      });
    }
  }

  editar(cita: CitaResponse): void {
    this.formCita = { ...cita };
    this.editarCitaId = cita.id;
  }

  eliminar(id: number): void {
    if (!confirm('¿Eliminar esta cita?')) return;
    this.citaService.eliminarCita(id).subscribe({
      next: () => this.cargarCitas(),
    });
  }

  cancelar(): void {
    this.formCita = {};
    this.editarCitaId = null;
  }
}
