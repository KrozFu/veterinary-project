import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-appointment',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register-appointment.component.html',
  styleUrl: './register-appointment.component.css',
})
export class RegisterAppointmentComponent implements OnInit {
  appointmentForm: FormGroup;
  isSubmitting = false;
  alertMessage: string = '';
  alertType: 'alert-success' | 'alert-error' = 'alert-success';
  mascotas: any[] = [];
  veterinarios: any[] = [];
  minDate: string;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    // Configurar fecha mínima (hoy)
    const now = new Date();
    this.minDate = now.toISOString().slice(0, 16);

    this.appointmentForm = this.fb.group({
      mascotaId: ['', Validators.required],
      veterinarioId: ['', Validators.required],
      fecha: ['', [Validators.required, this.futureDateValidator]],
      duracion: ['30', Validators.required],
      motivo: ['', [Validators.required, Validators.maxLength(200)]],
    });
  }

  ngOnInit(): void {
    this.fetchMascotas();
    this.fetchVeterinarios();
  }

  // Validador personalizado para fechas futuras
  futureDateValidator(control: any) {
    const selectedDate = new Date(control.value);
    const now = new Date();
    return selectedDate > now ? null : { pastDate: true };
  }

  isFieldInvalid(field: string): boolean {
    const control = this.appointmentForm.get(field);
    return (
      !!control && control.invalid && (control.touched || this.isSubmitting)
    );
  }

  getErrorMessage(field: string): string {
    const control = this.appointmentForm.get(field);
    if (!control) return '';

    if (control.hasError('required')) {
      return 'Este campo es requerido';
    } else if (control.hasError('maxlength')) {
      return `Máximo ${control.errors?.['maxlength'].requiredLength} caracteres`;
    } else if (control.hasError('pastDate')) {
      return 'La cita debe ser en una fecha futura';
    }
    return '';
  }

  fetchMascotas(): void {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any[]>('http://localhost:9000/api/mascotas/mascotas', { headers })
      .subscribe({
        next: (res) => (this.mascotas = res),
        error: (err) => {
          console.error('Error al cargar mascotas:', err);
          this.showAlert('Error al cargar mascotas', 'error');
        },
      });
  }

  fetchVeterinarios(): void {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any[]>('http://localhost:9000/api/veterinarios', { headers })
      .subscribe({
        next: (res) => (this.veterinarios = res),
        error: (err) => {
          console.error('Error al cargar veterinarios:', err);
          this.showAlert('Error al cargar veterinarios', 'error');
        },
      });
  }

  showAlert(message: string, type: 'success' | 'error'): void {
    this.alertMessage = message;
    this.alertType = type === 'success' ? 'alert-success' : 'alert-error';
    setTimeout(() => this.dismissAlert(), 5000);
  }

  dismissAlert(): void {
    this.alertMessage = '';
  }

  resetForm(): void {
    this.appointmentForm.reset({
      duracion: '30',
    });
  }

  onSubmit(): void {
    this.isSubmitting = true;

    if (this.appointmentForm.invalid) {
      this.showAlert(
        'Por favor complete todos los campos correctamente',
        'error'
      );
      this.isSubmitting = false;
      return;
    }

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .post('http://localhost:9000/api/cita', this.appointmentForm.value, {
        headers,
      })
      .subscribe({
        next: () => {
          this.showAlert('Cita registrada correctamente', 'success');
          this.resetForm();
          setTimeout(() => this.router.navigate(['/citas']), 1500);
        },
        error: (err) => {
          console.error('Error al registrar cita:', err);
          const errorMsg = err.error?.message || 'Error al registrar cita';
          this.showAlert(errorMsg, 'error');
          this.isSubmitting = false;
        },
        complete: () => (this.isSubmitting = false),
      });
  }
}
