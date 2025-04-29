import { Component, OnInit } from '@angular/core';
import { AuthUtilsService } from '../../Service/auth-utils.service';
import { AuthService } from '../../Service/auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import {NgClass, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  imports: [
    NgClass,
    FormsModule,
    NgIf
  ],
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  userInfo: any = {};
  showEdit = false;
  editData: any = {};
  showRoleChange = false;
  newRole: string = '';
  selectedImage: File | null = null;
  previewImage: any = null;
  currentUserRole: string | null = '';
  currentUserEmail: string = '';


  constructor(
    private authUtilsService: AuthUtilsService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const routeEmail = params.get('email'); // this will be 'self' or actual email
      const tokenEmail = this.authUtilsService.getUserEmail();
      const userEmail = routeEmail === 'self' ? tokenEmail : routeEmail;

      if (!userEmail) {
        console.error('User email is null. Redirecting...');
        this.router.navigate(['/login']);
        return;
      }
       this.currentUserRole = this.authUtilsService.getUserRole();
      this.currentUserEmail = tokenEmail || '';

      // console.log(currentUserRole);
      this.loadUserProfile(userEmail);
    });
  }

  loadUserProfile(userEmail: string) {
    debugger;
    this.authService.getUserProfile(userEmail).subscribe({
      next: (response) => {
        const user = response?.data || response;
        if (user && user.userId) {
          this.userInfo = user;
          console.log('loaded user',this.userInfo);
        } else {
          console.warn('Unexpected user data format:', response);
        }
      },
      error: (error) => {
        console.error('Error loading profile:', error);
        this.router.navigate(['/error']);
      }
    });
  }

  openEditModal() {
    this.editData = { ...this.userInfo };
    this.showEdit = true;
  }

  closeEditModal() {
    this.showEdit = false;
    this.editData = {};
    this.selectedImage = null;
    this.previewImage = null;
  }

  onImageChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedImage = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.previewImage = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  updateProfile() {
    const formData = new FormData();
    console.log(this.userInfo);

    formData.append("userEmail", this.userInfo.userEmail || '');

    formData.append("userName", this.editData.userName || this.userInfo.userName || '');

    if (this.editData.userPassword?.trim()) {
      formData.append("userPassword", this.editData.userPassword);
    }

    if (this.selectedImage) {
      formData.append("profilePicture", this.selectedImage);
    }

    formData.append("role", this.editData.role || this.userInfo.role || 'USER');
    formData.append("status", (this.editData.status ?? this.userInfo.status).toString());
    if (this.editData.staffPhone) {
      formData.append("staffPhone", this.editData.staffPhone);
    }

    if (this.editData.staffAddress) {
      formData.append("staffAddress", this.editData.staffAddress);
    }

    if (this.editData.approveDate) {
      formData.append("approveDate", this.editData.approveDate);
    }

    if (this.editData.registrationDate) {
      formData.append("registrationDate", this.editData.registrationDate);
    }

    console.log("FormData being submitted:");
    formData.forEach((value, key) => console.log(`${key}:`, value));

    this.authService.editUser(formData).subscribe({
      next: (response ) => {
        debugger;
        console.log("Profile updated",response);

        this.loadUserProfile(response.data.userEmail);
        this.closeEditModal();
      },
      error: (err) => {
        console.error("Edit failed", err);
      }
    });
  }
  openRoleChangeModal() {
    this.newRole = this.userInfo.role; // default pre-selected
    this.showRoleChange = true;
  }

  closeRoleChangeModal() {
    this.showRoleChange = false;
    this.newRole = '';
  }

  changeRole() {
    if (!this.newRole || this.newRole === this.userInfo.role) {
      alert("Please select a different role!");
      return;
    }

    const roleChangeRequest = {
      userEmail: this.userInfo.userEmail,
      newRole: this.newRole
    };

    this.authService.changeUserRole(roleChangeRequest).subscribe({
      next: (response) => {
        console.log("Role changed successfully", response);
        alert("Role updated successfully!");
        this.loadUserProfile(this.userInfo.userEmail);
        this.closeRoleChangeModal();
      },
      error: (error) => {
        console.error("Failed to change role", error);
        alert("Role update failed!");
      }
    });
  }

  deleteProfile() {
    if (confirm("Are you sure you want to delete this profile?")) {
      this.authService.deleteUserByEmail(this.userInfo.userEmail,this.currentUserEmail ).subscribe({
        next: () => {
          alert("Deleted successfully");
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error("Delete failed", err);
        }
      });
    }
  }
}
