import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Service/auth.service';
import { NgForOf, NgIf } from '@angular/common';
import { DecodeTokenService } from '../../Service/DecodeToken/decode-token.service';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-credit-history',
  templateUrl: './credit-history.component.html',
  imports: [
    NgIf,
    NgForOf
  ],
  styleUrls: ['./credit-history.component.css']
})
export class CreditHistoryComponent implements OnInit {
  creditHistories: any[] = [];
  loading: boolean = true;

  constructor(
    private authService: AuthService,
    private decodeTokenService: DecodeTokenService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.fetchCreditHistory();
  }

  fetchCreditHistory() {
    const UserEmail = this.decodeTokenService.getUser();
    this.authService.getCreditHistory(UserEmail).subscribe({
      next: (response: any) => {
        this.loading = false;
        if (response.status) {
          this.creditHistories = response.data;
          this.toastr.success('Credit history loaded successfully', 'Success');
          this.cdr.detectChanges();
        } else {
          this.toastr.warning('No credit history found', 'Warning');
        }
      },
      error: (error) => {
        this.loading = false;
        console.error('Error fetching credit history:', error);
        this.toastr.error('Error fetching credit history', 'Error');
      }
    });
  }
}
