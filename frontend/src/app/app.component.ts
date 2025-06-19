import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { LoginModalComponent } from './components/login-modal/login-modal.component';
import { RegisterModalComponent } from './components/register-modal/register-modal.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HeaderComponent,
    FooterComponent,
    LoginModalComponent,
    RegisterModalComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'VetCare';

  constructor(private router: Router) {
    this.autoRedirectIfAuthenticated();
  }

  autoRedirectIfAuthenticated(): void {
    const token = localStorage.getItem('token');
    if (token) {
      const decoded = this.decodeJWT(token);
      const roles = decoded.roles;
      this.redirectByRole(roles);
    }
  }

  decodeJWT(token: string): any {
    const payload = token.split('.')[1];
    const decoded = atob(payload);
    return JSON.parse(decoded);
  }

  redirectByRole(roles: string[]): void {
    if (roles.includes('ADMIN')) {
      this.router.navigate(['/dashboard']);
    } else if (roles.includes('VETERINARIO')) {
      this.router.navigate(['/veterinary-dashboard']);
    } else if (roles.includes('CLIENTE')) {
      this.router.navigate(['/client-dashboard']);
    }
  }
}
