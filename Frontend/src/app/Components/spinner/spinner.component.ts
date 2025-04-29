// spinner.component.ts
import { Component } from '@angular/core';
import { LoadingService } from '../../Service/Loading/loading.service';
import {AsyncPipe, NgIf} from '@angular/common';

@Component({
  selector: 'app-spinner',
  template: `
    <div class="spinner-overlay" *ngIf="loadingService.loading$ | async">
      <div class="spinner"></div>
    </div>
  `,
  imports: [
    NgIf,
    AsyncPipe
  ],
  styleUrls: ['./spinner.component.css']
})
export class SpinnerComponent {
  constructor(public loadingService: LoadingService) {}
}
