import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RegisterPetComponent } from '../../components/register-pet/register-pet.component';
import { ListPetsComponent } from '../../components/list-pets/list-pets.component';

@Component({
  selector: 'app-dash-pets',
  standalone: true,
  imports: [CommonModule, RegisterPetComponent, ListPetsComponent],
  templateUrl: './dash-pets.component.html',
  styleUrls: ['./dash-pets.component.css'],
})
export class DashPetsComponent {
  section: string = 'regpets';

  toggleSection(section: string): void {
    this.section = section;
  }
}
