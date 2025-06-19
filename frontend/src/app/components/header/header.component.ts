import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, DatePipe],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  lastAccess: Date = new Date();

  constructor(public authService: AuthService) {}

  ngOnInit(): void {
    // Simulamos obtener el último acceso (en una app real vendría del servicio)
    if (this.authService.isAuthenticated()) {
      this.lastAccess = new Date();
    }
  }

  logout(): void {
    this.authService.logout();
    // Redirigir al home después de cerrar sesión
    window.location.href = '/';
  }
}
