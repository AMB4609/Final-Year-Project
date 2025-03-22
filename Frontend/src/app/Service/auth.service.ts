import { Injectable } from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, of, tap} from 'rxjs';
import {AuthResponse} from '../Model/interface/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  registerUrl = 'http://localhost:9091/api/register'
  private roles: Array<string> = [];
  private apiUrl = 'http://localhost:9091/api/login/loginUser';
  constructor(private http: HttpClient, private router: Router) { }

  login(userEmail: string, userPassword: string):Observable<AuthResponse | null> {
    debugger;
    return this.http.post<AuthResponse>(`${this.apiUrl}`, { userEmail, userPassword }, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    }).pipe(
      tap(res => {
          localStorage.setItem('token', res.token);
      }),
      catchError(error => {
        console.error('Login error:', error);
        return of(null);
      })
    );
  }

  register(formValue : any) : Observable<null> {
    console.log(formValue);
    return this.http.post<null>(`${this.registerUrl}/registerUser`,formValue);
  }
}



