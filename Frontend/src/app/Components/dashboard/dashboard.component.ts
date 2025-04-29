import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Service/auth.service';
import { AuthUtilsService } from '../../Service/auth-utils.service';
import { ChartConfiguration } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';
import { DecimalPipe, NgClass, NgForOf, NgIf } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  imports: [
    NgChartsModule,
    DecimalPipe,
    NgClass,
    NgForOf,
    NgIf
  ],
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  top5Predictions: any[] = [];
  loading = true;
  averageCreditScore: number = 0;
  latestRecommendations: string[] = [];

  lineChartData: ChartConfiguration<'line'>['data'] = {
    labels: [],
    datasets: [
      {
        data: [],
        label: 'Credit Score Trend',
        fill: true,
        tension: 0.4,
        borderWidth: 3,
        backgroundColor: 'rgba(58, 12, 163, 0.2)',
        borderColor: 'rgba(58, 12, 163, 1)',
        pointBackgroundColor: 'rgba(58, 12, 163, 1)'
      }
    ]
  };

  lineChartOptions: ChartConfiguration<'line'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: {
        ticks: {
          autoSkip: true,
          maxRotation: 45,
          minRotation: 0
        }
      },
      y: {
        beginAtZero: true,
        suggestedMax: 1000
      }
    },
    plugins: {
      legend: {
        display: true,
        position: 'top'
      }
    }
  };

  riskPieData: ChartConfiguration<'pie'>['data'] = {
    labels: ['Low Risk', 'Medium Risk', 'High Risk'],
    datasets: [
      {
        data: [0, 0, 0],
        backgroundColor: [
          '#28a745', // Green
          '#ffc107', // Yellow
          '#dc3545'  // Red
        ],
        borderColor: [
          '#28a745',
          '#ffc107',
          '#dc3545'
        ],
        borderWidth: 2
      }
    ]
  };

  riskPieOptions: ChartConfiguration<'pie'>['options'] = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: 'top'
      }
    }
  };

  constructor(private authService: AuthService, private authUtils: AuthUtilsService) {}

  ngOnInit(): void {
    const email = this.authUtils.getUserEmail();
    if (email) {
      this.authService.getCreditHistory(email).subscribe({
        next: (response) => {
          let allPredictions: any[] = [];

          try {
            allPredictions = typeof response.data === 'string'
              ? JSON.parse(response.data)
              : response.data ?? [];
          } catch (e) {
            console.error('Failed to parse prediction data', e);
            allPredictions = [];
          }

          this.top5Predictions = allPredictions
            .sort((a, b) => new Date(b.predictionDate).getTime() - new Date(a.predictionDate).getTime())
            .slice(0, 5);

          const fullHistory = allPredictions
            .sort((a, b) => new Date(a.predictionDate).getTime() - new Date(b.predictionDate).getTime());

          this.lineChartData.labels = fullHistory.map(item => item.predictionDate);
          this.lineChartData.datasets[0].data = fullHistory.map(item => item.creditScore);

          const today = new Date();
          const lastMonth = new Date();
          lastMonth.setDate(today.getDate() - 30);

          const scoresLastMonth = allPredictions.filter(item => {
            const predictionDate = new Date(item.predictionDate);
            return predictionDate >= lastMonth && predictionDate <= today;
          }).map(item => item.creditScore);

          const sumScores = scoresLastMonth.reduce((sum, score) => sum + score, 0);
          this.averageCreditScore = scoresLastMonth.length ? sumScores / scoresLastMonth.length : 0;

          // Grouping Logic: combine normal and Basic categories
          const lowRisk = allPredictions.filter(p => p.riskCategory.includes('LowRisk')).length;
          const mediumRisk = allPredictions.filter(p => p.riskCategory.includes('MediumRisk')).length;
          const highRisk = allPredictions.filter(p => p.riskCategory.includes('HighRisk')).length;

          this.riskPieData.datasets[0].data = [
            lowRisk,
            mediumRisk,
            highRisk
          ];

          this.loading = false;
          if (allPredictions.length > 0 && allPredictions[0].recommendations?.length > 0) {
            this.latestRecommendations = allPredictions[0].recommendations.map((rec: any) => rec.recommendationText);
          } else {
            this.latestRecommendations = ['No recommendations available.'];
          }

        },
        error: (error) => {
          console.error('Error fetching credit history', error);
          this.loading = false;
        }
      });
    } else {
      console.error('No email found in token.');
      this.loading = false;
    }
  }
}
