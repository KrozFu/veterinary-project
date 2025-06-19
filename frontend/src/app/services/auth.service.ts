import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getDecodedToken(): any {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = atob(token.split('.')[1]);
      return JSON.parse(payload);
    } catch {
      return null;
    }
  }

  getUsername(): string | null {
    const email = this.getDecodedToken()?.sub;
    return email ? email.split('@')[0] : null;
  }

  getRoles(): string[] {
    const decoded = this.getDecodedToken();
    return decoded?.roles || [];
  }

  /**
   * Devuelve el primer rol del usuario.
   * Útil cuando hay un solo rol por usuario.
   */
  getUserRole(): string | null {
    const roles = this.getRoles();
    return roles.length > 0 ? roles[0] : null;
  }

  /**
   * Verifica si el usuario tiene un rol específico.
   */
  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem('token');
    window.location.href = '/';
  }
}
