import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    const token = localStorage.getItem('token');

    if (!token) {
      console.warn('No hay token. Redirigiendo al inicio.');
      this.router.navigate(['/']);
      return false;
    }

    const user = this.decodeJWT(token);
    const userRoles: string[] = user.roles || [];
    const expectedRoles: string[] = route.data['roles'];

    console.log('Roles del usuario:', userRoles);
    console.log('Roles requeridos para la ruta:', expectedRoles);

    if (expectedRoles && expectedRoles.length > 0) {
      const hasAccess = expectedRoles.some((role) => userRoles.includes(role));

      if (!hasAccess) {
        console.warn('Acceso denegado. Rol insuficiente.');
        this.router.navigate(['/']);
        return false;
      }
    }

    return true;
  }

  private decodeJWT(token: string): any {
    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch (e) {
      console.error('Error decodificando token', e);
      return {};
    }
  }
}
