import { Component, inject } from '@angular/core';
import { AuthService } from '../../Service/auth.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import {NgClass, NgIf} from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf, NgClass],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  userForm: FormGroup;
  registerUser: AuthService = inject(AuthService);
  router = inject(Router);

  showPassword = false;
  showConfirmPassword = false;

  constructor() {
    this.userForm = new FormGroup(
      {
        userName: new FormControl('', [Validators.required, Validators.minLength(2)]),
        userEmail: new FormControl('', [Validators.required, Validators.email]),
        userPassword: new FormControl('', [Validators.required, Validators.minLength(6)]),
        confirmPassword: new FormControl('', [Validators.required])
      },
      { validators: passwordsMatchValidator }
    );
  }

  getUser() {
    if (this.userForm.invalid) {
      console.log('Error: Form is invalid.');
      return;
    }

    this.registerUser.register(this.userForm.value).subscribe({
      next: () => {
        console.log("Register success");
        this.router.navigate(['dashboard']);
      },
      error: (error) => {
        console.error('Registration failed:', error);
      }
    });
  }

  // Toggle password visibility
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }
}

// Custom validator function for password matching
const passwordsMatchValidator: ValidatorFn = (formGroup: AbstractControl): ValidationErrors | null => {
  const password = formGroup.get('userPassword')?.value;
  const confirmPassword = formGroup.get('confirmPassword')?.value;

  return password === confirmPassword ? null : { passwordsMismatch: true };
};
