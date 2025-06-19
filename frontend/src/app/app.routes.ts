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
    path: 'admin/manage-clients',
    loadComponent: () =>
      import('./pages/admin-dashboard/admin-dashboard.component').then(
        (m) => m.AdminDashboardComponent
      ),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'admin/dash-pets',
    loadComponent: () =>
      import('./pages/dash-pets/dash-pets.component').then(
        (m) => m.DashPetsComponent
      ),
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'CLIENTE'] },
  },
  {
    path: '**',
    redirectTo: '',
  },
];
