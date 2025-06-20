import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-list-pets',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './list-pets.component.html',
  styleUrls: ['./list-pets.component.css'],
})
export class ListPetsComponent implements OnInit {
  mascotas: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.cargarMascotas();
  }

  cargarMascotas(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('No estás autenticado');
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any[]>('http://localhost:9000/api/mascotas/mascotas', { headers })
      .subscribe({
        next: (data) => {
          this.mascotas = data;
        },
        error: (err) => {
          console.error('Error al cargar mascotas:', err);
          alert('Error al obtener mascotas');
        },
      });
  }

  getImagenUrl(fotoUrl: string): string {
    return fotoUrl
      ? 'http://localhost:9000' + fotoUrl
      : 'http://localhost:9000/imagenes/default.png';
  }

  reemplazarPorDefecto(event: Event): void {
    const img = event.target as HTMLImageElement;
    img.src = 'http://localhost:9000/imagenes/default.png';
  }
}
