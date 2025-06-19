import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';

declare var bootstrap: any;

@Component({
  selector: 'app-login-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login-modal.component.html',
  styleUrl: './login-modal.component.css',
})
export class LoginModalComponent {
  loginForm: FormGroup;
  showPassword: boolean = false;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const loginData = this.loginForm.value;
      this.http
        .post<any>('http://localhost:9000/api/auth/login', loginData)
        .subscribe({
          next: (response) => {
            const token = response.token;

            localStorage.setItem('token', token);

            const decoded = this.decodeJWT(token);
            const roles = decoded.roles;

            this.closeModal();
            this.redirectByRole(roles);
          },
          error: (error) => {
            console.error('Error en el login: ', error);
          },
        });
    }
  }

  decodeJWT(token: string): any {
    const payload = token.split('.')[1];
    const decoded = atob(payload);
    return JSON.parse(decoded);
  }

  redirectByRole(roles: string[]): void {
    if (roles.includes('ADMIN')) {
      this.router.navigate(['/admin-dashboard']);
    } else if (roles.includes('MEDICO')) {
      this.router.navigate(['/veterinary-dashboard']);
    } else if (roles.includes('CLIENTE')) {
      this.router.navigate(['/client-dashboard']);
    } else {
      this.router.navigate(['/']);
    }
  }

  closeModal(): void {
    const modalElemnt = document.getElementById('loginModal');
    const modal = bootstrap.Modal.getInstance(modalElemnt);
    modal?.hide();
  }

  openRegisterModal(): void {
    this.closeModal();
  }
}
