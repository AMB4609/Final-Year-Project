import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Service/auth.service';
import { DecodeTokenService } from '../../Service/DecodeToken/decode-token.service';
import { NgForOf, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  standalone: true,
  imports: [NgForOf, NgIf, FormsModule],
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {
  pendingUsers: any[] = [];
  staffReports: any[] = [];
  userReports: any[] = [];
  role: string = '';

  constructor(private authService: AuthService, private decodeTokenService: DecodeTokenService,) {}

  ngOnInit(): void {
    debugger;
    this.role = this.decodeTokenService.getRole();
    console.log("Current Role: ",this.role);
    this.fetchPendingUsers();
    this.fetchReports();
  }

  fetchPendingUsers() {
    this.authService.getPendingUsers().subscribe(response => {
      if (response.status) {
        this.pendingUsers = response.data;
      }
    });
  }

  fetchReports() {
    const UserEmail = this.decodeTokenService.getUser();
debugger;
    if (this.role == 'STAFF') {
      this.authService.getReportsForStaff().subscribe(response => {
debugger;
        if (response.status && Array.isArray(response.data)) {
          debugger;
          this.staffReports = response.data.map(report => ({ ...report, recommendationInput: '' }));
        }
      });
    } else {
      this.authService.getReportsForUser(UserEmail).subscribe(response => {
        if (response.status && Array.isArray(response.data)) {
          debugger;
          this.userReports = response.data;
        }
      });
    }
  }

  approve(userId: string) {
    this.authService.approveUser(userId).subscribe(() => {
      this.fetchPendingUsers();
    });
  }

  reject(userId: string) {
    this.authService.rejectUser(userId).subscribe(() => {
      this.fetchPendingUsers();
    });
  }

  downloadPDF(reportId: number) {
    this.authService.downloadPDF(reportId).subscribe((data: Blob) => {
      const blob = new Blob([data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `report-${reportId}.pdf`;
      link.click();
      window.URL.revokeObjectURL(url);
    });
  }

  appendRecommendation(id: number, notes: string) {
    if (!notes || notes.trim() === '') return;
    this.authService.appendRecommendation(id, notes).subscribe(response => {
      if (response.status) {
        this.fetchReports();
      }
    });
  }
}
