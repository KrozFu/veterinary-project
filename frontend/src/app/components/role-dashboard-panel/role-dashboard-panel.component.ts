import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-role-dashboard-panel',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './role-dashboard-panel.component.html',
  styleUrls: ['./role-dashboard-panel.component.css'],
})
export class RoleDashboardPanelComponent implements OnInit {
  role: string | null = null;

  // Opciones para ADMIN
  adminOptions = [
    {
      title: 'Gestionar Clientes',
      route: '/admin/manage-clients',
      icon: 'bi-person-lines-fill',
      description: 'Administra los clientes registrados en el sistema',
      color: 'primary',
    },
    {
      title: 'Listar Veterinarios',
      route: '/admin/manage-list-vets',
      icon: 'bi-person-lines-fill',
      description: 'Lista los Veterinarios registrados en el sistema',
      color: 'primary',
    },
    {
      title: 'Registrar Veterinarios',
      route: '/admin/manage-vets',
      icon: 'bi-person-plus-fill',
      description: 'Agrega nuevos profesionales al equipo veterinario',
      color: 'info',
    },
    {
      title: 'Administrar Mascotas',
      route: '/admin/dash-pets',
      icon: 'bi-paw-fill',
      description: 'Gestiona el registro de pacientes animales',
      color: 'success',
    },
  ];

  // Opciones para CLIENTE
  clientOptions = [
    {
      title: 'Registrar Mascota',
      route: '/admin/register-pets',
      icon: 'bi-plus-circle',
      description: 'Agrega una nueva mascota a tu perfil',
      color: 'success',
    },
    {
      title: 'Agendar Cita',
      route: '/client/appointments',
      icon: 'bi-calendar-check',
      description: 'Solicita una cita para tu mascota',
      color: 'primary',
    },
    {
      title: 'Mis Mascotas',
      route: '/admin/list-pets',
      icon: 'bi-heart-fill',
      description: 'Revisa el historial de tus mascotas',
      color: 'warning',
    },
  ];

  // Opciones para VETERINARIO
  vetOptions = [
    {
      title: 'Historial Clínico',
      route: '/veterinary/clinical-history',
      icon: 'bi-journal-medical',
      description: 'Accede a los registros médicos de los pacientes',
      color: 'primary',
    },
    {
      title: 'Citas Programadas',
      route: '/veterinary/appointments',
      icon: 'bi-calendar-date',
      description: 'Revisa tu agenda de consultas',
      color: 'info',
    },
    {
      title: 'Expedientes',
      route: '/veterinary/records',
      icon: 'bi-folder2-open',
      description: 'Administra los expedientes médicos',
      color: 'warning',
    },
  ];

  constructor(public authService: AuthService) {}

  ngOnInit(): void {
    this.role = this.authService.getUserRole();
  }
}
