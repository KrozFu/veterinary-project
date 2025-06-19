import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-register-pet',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register-pet.component.html',
  styleUrl: './register-pet.component.css',
})
export class RegisterPetComponent {
  petForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.petForm = this.fb.group({
      nombre: ['', Validators.required],
      edad: [0, [Validators.required, Validators.min(0)]],
      raza: ['', Validators.required],
      color: ['', Validators.required],
      tipo: ['', Validators.required],
      descripcion: [''],
      fotoUrl: [''],
    });
  }

  onSubmit(): void {
    if (this.petForm.valid) {
      const token = localStorage.getItem('token');
      if (!token) {
        alert('No estás autenticado');
        return;
      }

      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

      console.log(this.petForm.value);
      this.http
        .post('http://localhost:9000/api/mascotas', this.petForm.value, {
          headers,
        })
        .subscribe({
          next: () => {
            alert('Mascota registrada con éxito');
            this.petForm.reset();
          },
          error: (err) => {
            console.error('Error al registrar Mascota:', err);
            alert('Error en el registro');
          },
        });
    } else {
      this.petForm.markAllAsTouched();
    }
  }
}
