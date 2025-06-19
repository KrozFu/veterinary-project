import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RegisterVetComponent } from '../../components/register-vet/register-vet.component';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RegisterVetComponent],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css',
})
export class AdminDashboardComponent {
  section: string = '';

  toggleSection(section: string): void {
    this.section = this.section === section ? '' : section;
  }
}
