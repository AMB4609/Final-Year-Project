import { Injectable } from '@angular/core';
import {jwtDecode} from 'jwt-decode';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class DecodeTokenService {
  private roles: string = '';
  private UserMail : string = '';
  private expiry : number = 0;

  constructor(private http: HttpClient, private router: Router) {
    this.loadToken();
  }
  loadToken() {
    debugger;
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = jwtDecode<any>(token);
      (decodedToken);
      this.roles = decodedToken.role ? decodedToken.role : "USER";
      this.UserMail = decodedToken.sub;
      this.expiry = decodedToken.exp;
      console.log(decodedToken);
    }
  }

  isTokenExpired(): boolean {

    const now = Date.now()/1000;
    return this.expiry < now;
  }

  hasRole(...role: string[]): boolean {
    return role.includes(this.roles);
  }

  hasAnyRole(requiredRoles: string[]): boolean {
    return requiredRoles.some(role => this.roles.includes(role));
  }
  getRole (): any {
    return this.roles;
    // const token = localStorage.getItem('token');
    // if (token) {
    //   const decodedToken = jwtDecode<any>(token);
    //   return decodedToken.role;
    // }
  }
  hasUser(User :string){
    return this.UserMail === User;
  }
  getUser(): string {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = jwtDecode<any>(token);
      return decodedToken.sub;
    }
    return '';
  }
}
