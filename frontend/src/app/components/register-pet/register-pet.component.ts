import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-pet',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register-pet.component.html',
  styleUrl: './register-pet.component.css',
})
export class RegisterPetComponent implements OnInit {
  petForm: FormGroup;
  selectedFile: File | null = null;
  previewUrl: string | ArrayBuffer | null = null;
  formSubmitted = false;
  alertMessage: string = '';
  alertType: 'alert-success' | 'alert-error' = 'alert-success';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.petForm = this.fb.group({
      nombre: ['', Validators.required, Validators.maxLength(50)],
      edad: [
        null,
        [Validators.required, Validators.min(0), Validators.max(30)],
      ],
      raza: ['', [Validators.required, Validators.maxLength(50)]],
      color: ['', [Validators.required, Validators.maxLength(30)]],
      tipo: ['', Validators.required],
      descripcion: ['', Validators.maxLength(200)],
    });
  }

  isFieldInvalid(field: string): boolean {
    const control = this.petForm.get(field);
    return (
      !!control && control.invalid && (control.touched || this.formSubmitted)
    );
  }

  getErrorMessage(field: string): string {
    const control = this.petForm.get(field);
    if (!control) return '';

    if (control.hasError('required')) {
      return 'Este campo es requerido';
    } else if (control.hasError('min')) {
      return `La edad mínima es ${control.errors?.['min'].min}`;
    } else if (control.hasError('max')) {
      return `El valor máximo permitido es ${control.errors?.['max'].max}`;
    } else if (control.hasError('maxlength')) {
      return `Máximo ${control.errors?.['maxlength'].requiredLength} caracteres`;
    }
    return '';
  }

  ngOnInit(): void {}

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];

      // Validar tamaño de archivo (max 2MB)
      if (file.size > 2 * 1024 * 1024) {
        this.showAlert('El tamaño máximo de la imagen es 2MB', 'error');
        input.value = '';
        return;
      }

      // Validar tipo de archivo
      if (!file.type.match('image.*')) {
        this.showAlert('Solo se permiten archivos de imagen', 'error');
        input.value = '';
        return;
      }

      this.selectedFile = file;

      // Mostrar vista previa
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  removeImage(): void {
    this.previewUrl = null;
    this.selectedFile = null;
    const fileInput = document.getElementById('foto') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  showAlert(message: string, type: 'success' | 'error'): void {
    this.alertMessage = message;
    this.alertType = type === 'success' ? 'alert-success' : 'alert-error';

    // Ocultar alerta después de 5 segundos
    setTimeout(() => {
      this.dismissAlert();
    }, 5000);
  }

  dismissAlert(): void {
    this.alertMessage = '';
  }

  resetForm(): void {
    this.petForm.reset();
    this.previewUrl = null;
    this.selectedFile = null;
    this.formSubmitted = false;
  }

  onSubmit(): void {
    this.formSubmitted = true;

    if (this.petForm.invalid) {
      this.showAlert(
        'Por favor complete todos los campos requeridos correctamente',
        'error'
      );
      return;
    }

    const formData = new FormData();

    formData.append(
      'mascota',
      new Blob([JSON.stringify(this.petForm.value)], {
        type: 'application/json',
      })
    );

    if (this.selectedFile) {
      formData.append('foto', this.selectedFile);
    }

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .post('http://localhost:9000/api/mascotas', formData, { headers })
      .subscribe({
        next: (res) => {
          this.showAlert('Mascota registrada correctamente', 'success');
          setTimeout(() => {
            this.router.navigate(['/mascotas']);
          }, 1500);
        },
        error: (err) => {
          console.error('Error al registrar mascota:', err);
          const errorMsg = err.error?.message || 'Error al registrar mascota';
          this.showAlert(errorMsg, 'error');
        },
      });
  }
}
