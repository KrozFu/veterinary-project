import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-register-vet',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './register-vet.component.html',
  styleUrls: ['./register-vet.component.css'],
})
export class RegisterVetComponent {
  vetForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.vetForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      identificacion: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      direccion: ['', Validators.required],
      especialidad: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  onSubmit(): void {
    if (this.vetForm.valid) {
      const vetData = { ...this.vetForm.value, rol: 'VETERINARIO' };

      const token = localStorage.getItem('token');
      if (!token) {
        alert('No estás autenticado');
        return;
      }

      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

      this.http
        .post('http://localhost:9000/api/auth/register-veterinario', vetData, {
          headers,
        })
        .subscribe({
          next: () => {
            alert('Veterinario registrado con éxito');
            this.vetForm.reset();
          },
          error: (err) => {
            console.error('Error al registrar veterinario:', err);
            alert('Error en el registro');
          },
        });
    }
  }
}
