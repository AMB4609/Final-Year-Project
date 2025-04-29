import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../Service/auth.service';
import {DatePipe, NgForOf} from '@angular/common';


@Component({
  selector: 'app-staff-dashboard',
  templateUrl: './staff-dashboard.component.html',
  imports: [
    DatePipe,
    NgForOf
  ],
  styleUrls: ['./staff-dashboard.component.css']
})
export class StaffDashboardComponent implements OnInit {

  latestActivities: {
    activityId?: number;
    activityDescription?: string;
    activityTime?: string;
    [key: string]: any;
  }[] = [];  // ✅ Directly define here

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    this.fetchLatestActivities();
  }

  fetchLatestActivities() {
    this.authService.getLatestActivities().subscribe((res: any) => {
      if (res.status) {
        this.latestActivities = res.data;
      }
    });
  }

  goToNotification() {
    this.router.navigate(['/notification']);
  }

  goToUserStaff() {
    this.router.navigate(['/User-Staff']);
  }

  goToMyActivities() {
    this.router.navigate(['/activity']);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userEmail');
    this.router.navigate(['/login']);
  }
}
