import { Routes } from '@angular/router';
import { RegisterComponent } from './auth/register/register.component';

export const routes: Routes = [
  { path: 'registro', component: RegisterComponent },
  { path: '**', redirectTo: 'registro' },
];
