import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

declare var bootstrap: any;

@Component({
  selector: 'app-register-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register-modal.component.html',
  styleUrl: './register-modal.component.css',
})
export class RegisterModalComponent {
  registerForm: FormGroup;
  showPassword: boolean = false;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.registerForm = this.fb.group({
      identificacion: ['', Validators.required],
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      direccion: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      // terms: [false, Validators.requiredTrue]
    });
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  get identificacion() {
    return this.registerForm.get('indentificacion');
  }
  get nombre() {
    return this.registerForm.get('nombre');
  }
  get apellido() {
    return this.registerForm.get('apellido');
  }
  get email() {
    return this.registerForm.get('email');
  }
  get direccion() {
    return this.registerForm.get('direccion');
  }
  get password() {
    return this.registerForm.get('password');
  }
  // get term() {
  //   return this.registerForm.get('term');
  // }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const registerData = this.registerForm.value;
      this.http
        .post('http://localhost:9000/api/auth/register', registerData)
        .subscribe({
          next: (response) => {
            console.log('Register Exitoso', response);
            this.closeModal();
          },
          error: (error) => {
            console.error('Error en el register', error);
          },
        });
    }
  }

  closeModal(): void {
    const modalElement = document.getElementById('registerModal');
    const modal = bootstrap.Modal.getInstance(modalElement);
    modal?.hide();
  }

  openLoginModal(): void {
    this.closeModal();
    setTimeout(() => {
      const loginModal = new bootstrap.Modal(
        document.getElementById('loginModal')
      );
      loginModal.show();
    }, 300);
  }
}
