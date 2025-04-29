import { Component, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../Service/auth.service';
import { Router, RouterLink } from '@angular/router';
import { NgClass } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import {AuthUtilsService} from '../../Service/auth-utils.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, NgClass, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  userEmail: string = '';
  userPassword: string = '';
  authService = inject(AuthService);
  authUtilsService = inject(AuthUtilsService);
  router = inject(Router);
  private toastr = inject(ToastrService);
  showPassword: boolean = false;

  async onLogin(): Promise<void> {
    if (!this.userEmail || !this.userPassword) {
      this.toastr.warning('Please enter both email and password!', 'Validation');
      return;
    }

    try {
      debugger;
      const success = await this.authService.login(this.userEmail, this.userPassword).toPromise();

      if (success) {
        // Show success toast and redirect on successful login
        this.toastr.success('Login Successful!', 'Welcome');
        const role = this.authUtilsService.getUserRole()
        console.log(role);
        if (role == 'USER') {
          this.router.navigate(['dashboard']);
        }else{
          this.router.navigate(['staff-dashboard']);
        }

      } else {
        // Show error toast if login fails
        this.toastr.error('Invalid email or password', 'Login Failed');
      }
    } catch (err) {
      // Handle any errors that occur during the login process
      console.error('Login error:', err);
      this.toastr.error('An error occurred while logging in', 'Error');
    }
  }

  // Toggle visibility of the password input field
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}
