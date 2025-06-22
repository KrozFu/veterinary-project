import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface CitaResponse {
  id: number;
  fecha: string;
  motivo: string;
  estado: string;
  nombreMascota: string;
  nombreVeterinario: string;
}

@Injectable({
  providedIn: 'root',
})
export class CitaService {
  private apiUrl = 'http://localhost:9000/api/cita';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getCitasPorRol(rol: string): Observable<CitaResponse[]> {
    let url = this.apiUrl;
    if (rol === 'CLIENTE') url += '/mis-citas';
    else if (rol === 'VETERINARIO') url += '/veterinario';
    return this.http.get<CitaResponse[]>(url, { headers: this.getHeaders() });
  }

  crearCita(data: any): Observable<CitaResponse> {
    return this.http.post<CitaResponse>(this.apiUrl, data, {
      headers: this.getHeaders(),
    });
  }

  actualizarCita(id: number, data: any): Observable<CitaResponse> {
    return this.http.put<CitaResponse>(`${this.apiUrl}/${id}`, data, {
      headers: this.getHeaders(),
    });
  }

  eliminarCita(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders(),
    });
  }
}
