import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportService } from '../../../core/report.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  kpis: any = null;
  monthly: any[] = [];
  categories: any[] = [];
  loading = true;
  errorMsg = '';

  constructor(private reportService: ReportService) {}

  ngOnInit() {
    this.reportService.dashboard().subscribe({
      next: (d) => this.kpis = d,
      error: (err) => this.errorMsg = err.status === 403
        ? 'Access denied — Admin/Staff role required.'
        : 'Could not load dashboard.'
    });

    this.reportService.monthlyRevenue().subscribe({
      next: (m) => { this.monthly = m; this.loading = false; },
      error: () => this.loading = false
    });

    this.reportService.salesByCategory().subscribe({
      next: (c) => this.categories = c
    });
  }

  // Bar chart height % — ⭐ pure CSS chart, library ඕන නෑ!
  get maxRevenue(): number {
    return Math.max(...this.monthly.map(m => m['revenue'] ?? 0), 1);
  }

  barHeight(revenue: number): number {
    return Math.max((revenue / this.maxRevenue) * 100, 4);
  }

  monthName(m: number): string {
    return ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'][m - 1] ?? m.toString();
  }

  // Donut chart — colors category ගානට
  categoryColors = ['#0071e3', '#ff9f0a', '#30d158', '#ff453a', '#5e5ce6', '#ff2d55'];

  get totalCategoryRevenue(): number {
    return this.categories.reduce((s, c) => s + (c['revenue'] ?? 0), 0);
  }
}