import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { StoreComponent } from './components/store/store.component';
import { AuthGuard } from './guards/auth.guard';
import { RoleDashboardPanelComponent } from './components/role-dashboard-panel/role-dashboard-panel.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'store', component: StoreComponent },

  {
    path: 'dashboard', // ruta unificada para todos los roles
    component: RoleDashboardPanelComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'CLIENTE', 'VETERINARIO'] },
  },
  {
    path: 'admin/manage-vets',
    loadComponent: () =>
      import('./components/register-vet/register-vet.component').then(
        (m) => m.RegisterVetComponent
      ),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  // ----------------- Revisar bien esto -----------------
  {
    path: 'admin/dash-pets',
    loadComponent: () =>
      import('./pages/dash-pets/dash-pets.component').then(
        (m) => m.DashPetsComponent
      ),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  // -------------------------------
  // Registrar Mascota y listar
  {
    path: 'admin/list-pets',
    loadComponent: () =>
      import('./components/list-pets/list-pets.component').then(
        (m) => m.ListPetsComponent
      ),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'CLIENTE'] },
  },
  {
    path: 'admin/register-pets',
    loadComponent: () =>
      import('./components/register-pet/register-pet.component').then(
        (m) => m.RegisterPetComponent
      ),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'CLIENTE'] },
  },
  // Final Registrar Mascota y listar
  // Consultar solo ADMIN los usuarios CLIENTE y VETERINARIO
  {
    path: 'admin/manage-clients',
    loadComponent: () =>
      import('./pages/admin-clientes.component').then(
        (m) => m.AdminClientesComponent
      ),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'admin/manage-list-vets',
    loadComponent: () =>
      import('./pages/admin-veterinarios.component').then(
        (m) => m.AdminVeterinariosComponent
      ),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  // Final Consultar los usuarios
  {
    path: 'client/register-appointment',
    loadComponent: () =>
      import(
        './components/register-appointment/register-appointment.component'
      ).then((m) => m.RegisterAppointmentComponent),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'CLIENTE'] },
  },
  {
    path: 'table/citas',
    loadComponent: () =>
      import('./components/citas/citas.component').then(
        (m) => m.CitasComponent
      ),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'CLIENTE'] },
  },
  {
    path: '**',
    redirectTo: '',
  },
];
