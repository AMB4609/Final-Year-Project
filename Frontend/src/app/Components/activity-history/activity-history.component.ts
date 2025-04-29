import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../Service/auth.service';
import {DatePipe, NgForOf} from '@angular/common';

@Component({
  selector: 'app-activity-history',
  templateUrl: './activity-history.component.html',
  standalone: true,
  imports: [NgForOf, DatePipe],
  styleUrls: ['./activity-history.component.css']
})
export class ActivityHistoryComponent implements OnInit {

  userEmail: string = '';
  activities: any[] = [];

  constructor(private route: ActivatedRoute, private authService: AuthService) {}

  ngOnInit() {
    debugger;
    this.userEmail = this.route.snapshot.paramMap.get('userEmail') || '';
    this.fetchActivities();
  }

  fetchActivities() {
    this.authService.getActivitiesByEmail(this.userEmail).subscribe({
      next: (response: any) => {
        if (response.status) {
          this.activities = response.data;
        }
      },
      error: (err) => {
        console.error('Error fetching activities:', err);
      }
    });
  }
}
