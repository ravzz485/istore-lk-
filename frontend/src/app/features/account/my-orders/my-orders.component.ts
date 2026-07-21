import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../../core/order.service';

@Component({
  selector: 'app-my-orders',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './my-orders.component.html',
  styleUrl: './my-orders.component.scss'
})
export class MyOrdersComponent implements OnInit {

  orders: any[] = [];
  loading = true;

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.orderService.myOrders().subscribe({
      next: (page) => { this.orders = page.content; this.loading = false; },
      error: () => this.loading = false
    });
  }

  statusColor(status: string): string {
    const map: Record<string, string> = {
      PENDING: '#ff9f0a', CONFIRMED: '#0a84ff', PROCESSING: '#5e5ce6',
      SHIPPED: '#5ac8fa', DELIVERED: '#30d158', COMPLETED: '#30d158',
      CANCELLED: '#ff453a'
    };
    return map[status] || '#86868b';
  }
}