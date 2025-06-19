import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RegisterVetComponent } from '../../components/register-vet/register-vet.component';

@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [CommonModule, RegisterVetComponent],
  templateUrl: './client-dashboard.component.html',
  styleUrl: './client-dashboard.component.css',
})
export class ClientDashboardComponent {}
