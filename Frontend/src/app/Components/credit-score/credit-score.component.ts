import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../Service/auth.service';
import { Chart, registerables } from 'chart.js';
import { NgClass, NgForOf, NgStyle } from '@angular/common';
import { ToastrService } from 'ngx-toastr';

Chart.register(...registerables);

@Component({
  selector: 'app-credit-score',
  templateUrl: './credit-score.component.html',
  imports: [NgForOf, NgClass],
  styleUrls: ['./credit-score.component.css']
})
export class CreditScoreComponent implements OnInit {
  creditData: any;
  riskColor: string = '';
  recommendation: string[] = [];

  constructor(
    private creditScoreService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.creditData = this.creditScoreService.getCreditResponse();

    if (!this.creditData) {
      this.toastr.error('Credit data not available. Redirecting to get credit data.', 'Error');
      this.router.navigate(['/get-credit']);
      return;
    }

    this.setRiskColor();
    this.prepareRecommendations();
    this.createCreditScoreChart();
    this.createRiskGaugeChart();
    this.toastr.success('Credit score and recommendations loaded successfully', 'Success');
  }

  setRiskColor() {
    switch (this.creditData.data.riskCategory) {
      case 'LowRisk':
        this.riskColor = 'green';
        break;
      case 'MediumRisk':
        this.riskColor = 'orange';
        break;
      case 'HighRisk':
        this.riskColor = 'red';
        break;
      default:
        this.riskColor = 'grey';
    }
  }

  prepareRecommendations() {
    const raw = this.creditData.data.recommendation;
    this.recommendation = raw.map((item: { recommendationText: any; }) => item.recommendationText);
  }

  createCreditScoreChart() {
    const score = this.creditData.data.creditScore;
    const maxScore = 900;

    const centerTextPlugin = {
      id: 'centerTextPlugin',
      afterDraw(chart: any) {
        const { ctx, chartArea } = chart;
        const score = chart.config.data.datasets[0].data[0];
        const centerX = (chartArea.left + chartArea.right) / 2;
        const centerY = (chartArea.top + chartArea.bottom) / 2;

        ctx.save();
        ctx.font = 'bold 28px Arial';
        ctx.fillStyle = '#333';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(Math.round(score).toString(), centerX, centerY);
        ctx.restore();
      }
    };

    new Chart('creditChart', {
      type: 'doughnut',
      data: {
        labels: ['Credit Score', 'Remaining'],
        datasets: [{
          data: [score, maxScore - score],
          backgroundColor: ['#4caf50', '#e0e0e0'],
          borderWidth: 1,
        }]
      },
      options: {
        animation: {
          animateRotate: true,
          animateScale: true,
          duration: 1200
        },
        cutout: '80%',
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: (context: any) => `${context.label}: ${context.raw}`
            },
            backgroundColor: '#333',
            titleColor: '#fff',
            bodyColor: '#fff'
          }
        }
      },
      plugins: [centerTextPlugin]
    });
  }

  createRiskGaugeChart() {
    const riskCategory = this.creditData.data.riskCategory;

    let riskValue = 0;
    let riskColor = '#e0e0e0'; // Default grey

    if (riskCategory.includes('LowRisk')) {
      riskValue = 33;
      riskColor = '#4caf50'; // Green
    } else if (riskCategory.includes('MediumRisk')) {
      riskValue = 66;
      riskColor = '#ffc107'; // Yellow
    } else if (riskCategory.includes('HighRisk')) {
      riskValue = 90;
      riskColor = '#f44336'; // Red
    }

    new Chart('riskGauge', {
      type: 'doughnut',
      data: {
        datasets: [{
          data: [riskValue, 100 - riskValue],
          backgroundColor: [riskColor, '#e0e0e0'],
          borderWidth: 0
        }]
      },
      options: {
        animation: {
          animateRotate: true,
          animateScale: true,
          duration: 1200
        },
        cutout: '80%',
        rotation: -90,
        circumference: 180,
        plugins: {
          legend: { display: false },
          tooltip: {
            enabled: true,
            callbacks: {
              label: (context: any) => {
                return `${riskCategory}: ${riskValue}% risk level`;
              }
            },
            backgroundColor: '#333',
            titleColor: '#fff',
            bodyColor: '#fff'
          }
        }
      }
    });
  }

}
