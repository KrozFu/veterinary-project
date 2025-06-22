import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-vet',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register-vet.component.html',
  styleUrls: ['./register-vet.component.css'],
})
export class RegisterVetComponent {
  vetForm: FormGroup;
  isSubmitting = false;
  alertMessage: string = '';
  alertType: 'alert-success' | 'alert-error' = 'alert-success';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.vetForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(50)]],
      apellido: ['', [Validators.required, Validators.maxLength(50)]],
      identificacion: ['', [Validators.required, Validators.maxLength(20)]],
      email: ['', [Validators.required, Validators.email]],
      direccion: ['', [Validators.required, Validators.maxLength(100)]],
      especialidad: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  isFieldInvalid(field: string): boolean {
    const control = this.vetForm.get(field);
    return (
      !!control && control.invalid && (control.touched || this.isSubmitting)
    );
  }

  getErrorMessage(field: string): string {
    const control = this.vetForm.get(field);
    if (!control) return '';

    if (control.hasError('required')) {
      return 'Este campo es requerido';
    } else if (control.hasError('email')) {
      return 'Ingrese un email válido';
    } else if (control.hasError('minlength')) {
      return `Mínimo ${control.errors?.['minlength'].requiredLength} caracteres`;
    } else if (control.hasError('maxlength')) {
      return `Máximo ${control.errors?.['maxlength'].requiredLength} caracteres`;
    }
    return '';
  }

  showAlert(message: string, type: 'success' | 'error'): void {
    this.alertMessage = message;
    this.alertType = type === 'success' ? 'alert-success' : 'alert-error';

    setTimeout(() => {
      this.dismissAlert();
    }, 5000);
  }

  dismissAlert(): void {
    this.alertMessage = '';
  }

  resetForm(): void {
    this.vetForm.reset();
    this.isSubmitting = false;
  }

  onSubmit(): void {
    this.isSubmitting = true;

    if (this.vetForm.invalid) {
      this.showAlert(
        'Por favor complete todos los campos requeridos correctamente',
        'error'
      );
      this.isSubmitting = false;
      return;
    }

    const vetData = {
      ...this.vetForm.value,
      rol: 'VETERINARIO',
    };

    const token = localStorage.getItem('token');
    if (!token) {
      this.showAlert('No estás autenticado', 'error');
      this.isSubmitting = false;
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .post<{ message: string }>(
        // 'http://localhost:9000/api/auth/register-veterinario',
        'http://localhost:9000/api/admin/usuarios/register-veterinario',
        vetData,
        { headers }
      )
      .subscribe({
        next: (res) => {
          this.vetForm.reset();
          this.isSubmitting = false;
          this.showAlert(res.message, 'success');
          this.isSubmitting = false;
          setTimeout(() => {
            this.router.navigate(['/admin/manage-vets']);
          }, 1500);
        },
        error: (err) => {
          console.error('Error al registrar veterinario:', err);
          const errorMsg =
            err.error?.message || 'Error al registrar veterinario';
          this.showAlert(errorMsg, 'error');
          this.isSubmitting = false;
        },
      });
  }
}
