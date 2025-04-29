import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, UrlTree } from '@angular/router';
import { AuthUtilsService } from './Service/auth-utils.service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(
    private router: Router,
    private authUtils: AuthUtilsService,
    private toastr: ToastrService
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    const expectedRoles: string[] = route.data['roles'];
    const userRole = this.authUtils.getUserRole();

    if (userRole && expectedRoles.includes(userRole)) {
      return true;
    } else {
      this.toastr.error('Unauthorized access. Please login with proper credentials.', 'Access Denied');
      return this.router.parseUrl('/login');
    }
  }
}
