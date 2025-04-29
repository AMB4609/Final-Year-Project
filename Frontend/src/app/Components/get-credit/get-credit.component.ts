import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../Service/auth.service';
import {FormsModule} from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-get-credit',
  templateUrl: './get-credit.component.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./get-credit.component.css']
})
export class GetCreditComponent {
  inputData = {
    creditUtilizationRatio: 0,
    creditHistoryAgeMonths: 0,
    numOfDelayedPayment: 0,
    numCreditInquiries: 0,
    creditMixNumber: 0
  };

  constructor(
    private creditScoreService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  onSubmit() {
    this.creditScoreService.predictCreditScore(this.inputData).subscribe({
      next: (response :any) => {
        if (response && response.status) {  // Assuming your API returns {status:true/false}
          this.creditScoreService.setCreditResponse(response);
          this.router.navigate(['/credit-score']);
        } else {
          this.toastr.error(response?.message || 'Failed to predict credit score.', 'Error');
        }
      },
      error: (error :any ) => {
        console.error('API Error:', error);
      }
    });
  }
}
