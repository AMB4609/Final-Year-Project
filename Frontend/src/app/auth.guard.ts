import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): boolean | UrlTree {
    const token = localStorage.getItem('token');

    if (token && !this.isTokenExpired(token)) {
      return true;
    } else {
      // Optionally clear token if expired
      localStorage.removeItem('token');

      // Redirect to login page
      return this.router.parseUrl('/login');
    }
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiry = payload.exp;
      const now = Math.floor(Date.now() / 1000);
      return expiry < now;
    } catch (e) {
      console.error('Error decoding token', e);
      return true; // If error decoding, consider expired
    }
  }
}
