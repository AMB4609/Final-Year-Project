import { Component, inject } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './Service/auth.service';
import { AuthUtilsService } from './Service/auth-utils.service';
import { filter } from 'rxjs';
import { NgForOf, NgIf } from '@angular/common';
import { ToastrModule } from 'ngx-toastr';
import {SpinnerComponent} from './Components/spinner/spinner.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLinkActive, RouterLink, NgIf, NgForOf, SpinnerComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  showNavBar: boolean = true;
  title = 'Frontend';
  authService: AuthService = inject(AuthService);
  authUtilsService: AuthUtilsService = inject(AuthUtilsService);
  router: Router = inject(Router);

  userRole: string | null = null;

  constructor() {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.userRole = this.authUtilsService.getUserRole();
      this.showNavBar = (this.userRole === 'USER') && !['/login', '/register', '/'].includes((event as NavigationEnd).urlAfterRedirects);


    });
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  navLinks = [
    { path: '/dashboard', label: 'Dashboard', roles: ['USER'] },
    { path: '/profile/self', label: 'Profile', roles: ['USER', 'STAFF', 'ADMIN'] },
    { path: '/credit-history', label: 'Credit History', roles: ['USER'] },
    { path: '/get-credit', label: 'Get Credit', roles: ['USER'] },
    { path: '/credit-summary', label: 'Credit Summary', roles: ['USER'] },
    { path: '/admin/manage-users', label: 'Manage Users', roles: ['ADMIN'] },
    { path: '/staff/assist-users', label: 'Assist Users', roles: ['STAFF'] }
  ];
}
