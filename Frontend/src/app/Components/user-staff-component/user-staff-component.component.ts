// user-staff.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Service/auth.service';
import {NgForOf, NgIf} from '@angular/common';
import {RouterLink} from '@angular/router';
import { AuthUtilsService } from '../../Service/auth-utils.service';

@Component({
  selector: 'app-user-staff',
  templateUrl: './user-staff-component.component.html',
  imports: [
    NgForOf,
    NgIf,
    RouterLink
  ],
  styleUrls: ['./user-staff-component.component.css']
})
export class UserStaffComponent implements OnInit {
  currentUser : string | null ='';
  showUsers: boolean = true;
  users: any[] = [];
  staff: any[] = [];
  role: string | '' | undefined;

  constructor(private authService: AuthService,  private authUtilsService: AuthUtilsService) {}

  ngOnInit(): void {
    this.fetchUsers();
    this.fetchStaff();
    this.currentUser = this.authUtilsService.getUserEmail();
    this.role = this.authUtilsService.getUserRole()|| '';
    console.log(this.role);
  }

  fetchUsers(): void {
    this.authService.getAllUsers().subscribe(response => {
      if (response.status) {
        this.users = response.data;
      }
    });
  }

  fetchStaff(): void {
    debugger;
    this.authService.getAllStaff().subscribe(response => {
      if (response.status) {
        this.staff = response.data;
      }
    });
  }

  toggleList(): void {
    this.showUsers = !this.showUsers;
  }


  deleteUser(email: string): void {
    this.authService.deleteUserByEmail(email,this.currentUser || '').subscribe(() => {
      this.fetchStaff();
      this.fetchUsers();
    });
  }


  deleteStaff(email: string): void {
    this.deleteUser(email);
  }
}
