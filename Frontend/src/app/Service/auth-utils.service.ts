import { Injectable } from '@angular/core';
import {jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthUtilsService {

  constructor() {}

  // Function to decode JWT token and get the "sub" (email)
  getDecodedToken(): any {
    const token: string | null = localStorage.getItem('token');

    if (token) {
      try {
        const decodedToken = jwtDecode(token);
        console.log('Token at decode time:', token);

        // Using the decoded function
        return decodedToken;  // This will contain 'sub', 'iat', 'exp', etc.
      } catch (error) {
        console.error('Error decoding token', error);
        return null;
      }
    }
    return null;
  }

  // Function to get 'sub' (email) from the decoded token
  getUserEmail(): string | null {
    debugger;
    const decodedToken = this.getDecodedToken();
    debugger;
    return decodedToken ? decodedToken.sub : null;  // 'sub' contains the email
  }

  // Function to check if the user is authenticated
  isAuthenticated(): boolean {
    return !!localStorage.getItem('authToken');
  }

  // Function to get user role from decoded token (if needed)
  getUserRole(): string | null {
    const decodedToken = this.getDecodedToken();
    return decodedToken ? decodedToken.role : null;
  }
}
