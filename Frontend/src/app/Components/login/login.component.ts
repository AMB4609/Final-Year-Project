import { Component, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../Service/auth.service';
import { Router } from '@angular/router';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, NgClass],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  userEmail: string = '';
  userPassword: string = '';
  authService = inject(AuthService);
  router = inject(Router);
  showPassword: boolean = false;

  onLogin(): void {
    if (!this.userEmail || !this.userPassword) {
      alert('Please enter both email and password!');
      return;
    }

    this.authService.login(this.userEmail, this.userPassword).subscribe({
      next: (success: any) => {
        if (success) {
          alert('Login Successful!');
          this.router.navigate(['dashboard']);
        } else {
          alert('Invalid email or password. Please try again.');
        }
      },
      error: (err) => {
        console.error('Login error:', err);
        alert('An error occurred while logging in.');
      }
    });
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}
