import { Component } from '@angular/core';
import { SliderComponent } from '../slider/slider.component';
import { PetInfoComponent } from '../pet-info/pet-info.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [SliderComponent, PetInfoComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  constructor() {}
}
