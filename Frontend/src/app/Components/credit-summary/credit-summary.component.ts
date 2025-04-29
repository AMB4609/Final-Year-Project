import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { jsPDF } from 'jspdf';
import { Chart } from 'chart.js';
import { AuthService } from '../../Service/auth.service';
import { FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-credit-summary',
  templateUrl: './credit-summary.component.html',
  standalone: true,
  imports: [FormsModule],
  styleUrls: ['./credit-summary.component.css']
})
export class CreditSummaryComponent implements OnInit {
  @ViewChild('chartCanvas', { static: false }) chartCanvas!: ElementRef<HTMLCanvasElement>;

  sendToStaff: boolean = false;
  startDate: string = '';
  endDate: string = '';
  includeCreditScore: boolean = false;
  includeRiskCategory: boolean = false;
  includeRecommendation: boolean = false;

  creditHistoryData: any[] = [];
  selectedHistory: any[] = [];
  userEmail: string = 'AMB@gmail.com';

  constructor(private authService: AuthService, private toastr: ToastrService) {}

  ngOnInit() {
    this.fetchCreditHistory();
  }

  fetchCreditHistory() {
    this.authService.getCreditHistory(this.userEmail).subscribe({
      next: (response: any) => {
        if (response && response.status) {
          this.creditHistoryData = response.data;
          this.toastr.success('Credit history loaded successfully', 'Success');
        } else {
          console.error('Failed to fetch credit history:', response.message);
          this.toastr.error('Failed to fetch credit history', 'Error');
        }
      },
      error: (error) => {
        console.error('Error fetching credit history:', error);
        this.toastr.error('Error fetching credit history', 'Error');
      }
    });
  }

  filterDataByDateRange() {
    if (this.startDate && this.endDate) {
      this.selectedHistory = this.creditHistoryData.filter(record => {
        const predictionDate = new Date(record.predictionDate);
        const start = new Date(this.startDate);
        const end = new Date(this.endDate);
        return predictionDate >= start && predictionDate <= end;
      });
    }
  }

  onSubmit() {
    this.filterDataByDateRange();

    if (this.selectedHistory.length === 0) {
      this.toastr.error('No data available in the selected date range', 'Error');
      return;
    }

    this.generatePDF(this.sendToStaff);
  }


  async generatePDF(sendToStaff: boolean) {
    const doc = new jsPDF('p', 'mm', 'a4');
    let startY = 20;

    doc.setFontSize(20);
    doc.setFont('helvetica', 'bold');
    doc.text('Credit Summary Report', 15, startY);
    doc.setLineWidth(0.5);
    doc.line(15, startY + 2, 195, startY + 2);
    startY += 10;

    for (let i = 0; i < this.selectedHistory.length; i++) {
      const history = this.selectedHistory[i];

      if (startY > 230) {
        doc.addPage();
        startY = 20;
      }

      doc.setFontSize(14);
      doc.text(`Record ${i + 1}`, 15, startY);
      doc.setFontSize(12);
      startY += 8;

      if (this.includeCreditScore) {
        doc.text(`Credit Score: ${history.creditScore}`, 20, startY);
        startY += 7;
      }

      if (this.includeRiskCategory) {
        doc.text(`Risk Category: ${history.riskCategory}`, 20, startY);
        startY += 7;
      }

      if (this.includeRecommendation && Array.isArray(history.recommendations) && history.recommendations.length > 0) {
        doc.setFont('helvetica', 'bold');
        doc.text('Recommendations:', 20, startY);
        startY += 7;
        doc.setFont('helvetica', 'normal');
        history.recommendations.forEach((rec: any) => {
          const recText = `• ${rec.recommendationText}`;
          const splitText = doc.splitTextToSize(recText, 160);
          doc.text(splitText, 25, startY);
          startY += (splitText.length * 6);
        });
      } else if (this.includeRecommendation) {
        doc.text('Recommendations: None', 20, startY);
        startY += 7;
      }

      // --- Now correctly wait for chart rendering ---
      const canvas = document.createElement('canvas');
      canvas.width = 400;
      canvas.height = 150;
      const ctx = canvas.getContext('2d')!;

      await this.createChart(history, ctx);

      const chartImage = canvas.toDataURL('image/png');
      doc.addImage(chartImage, 'PNG', 20, startY, 160, 60);
      startY += 70;

      doc.setDrawColor(180);
      doc.line(15, startY, 195, startY);
      startY += 10;
    }

    const pdfBlob = doc.output('blob');
    const pdfFile = new File([pdfBlob], 'Credit_Summary_Report.pdf', { type: 'application/pdf' });

    if (sendToStaff) {
      this.authService.requestRecommendation(this.userEmail, pdfFile).subscribe({
        next: () => {
          this.toastr.success('PDF uploaded and recommendation requested!', 'Success');
        },
        error: (err) => {
          console.error('Failed to upload PDF for recommendation:', err);
          this.toastr.error('PDF upload for recommendation failed!', 'Error');
        }
      });
    } else {
      this.authService.storeReportWithoutRecommendation(this.userEmail, pdfFile).subscribe({
        next: () => {
          this.toastr.success('PDF stored successfully!', 'Success');
          const downloadLink = document.createElement('a');
          downloadLink.href = URL.createObjectURL(pdfFile);
          downloadLink.download = 'Credit_Summary_Report.pdf';
          downloadLink.click();
          URL.revokeObjectURL(downloadLink.href);
        },
        error: (err) => {
          console.error('Failed to store PDF:', err);
          this.toastr.error('PDF store failed!', 'Error');
        }
      });
    }
  }


  createChart(history: any, ctx: CanvasRenderingContext2D): Promise<void> {
    return new Promise((resolve) => {
      new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['Credit Score', 'Remaining to 900'],
          datasets: [{
            label: 'Credit Value',
            data: [history.creditScore, 900 - history.creditScore],
            backgroundColor: ['#4caf50', '#e0e0e0']
          }]
        },
        options: {
          animation: {
            onComplete: () => resolve()
          },
          responsive: false,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false
            }
          },
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      });
    });
  }
}
