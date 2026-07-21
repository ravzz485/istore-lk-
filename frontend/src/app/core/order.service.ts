import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Order {
  id: string;
  orderNo: string;
  items: any[];
  fulfilmentMethod: string;
  deliveryAddress: string;
  paymentMethod: string;
  status: string;
  total: number;
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class OrderService {

  private apiUrl = 'http://localhost:8081/api/v1/orders';

  constructor(private http: HttpClient) {}

  checkout(fulfilmentMethod: string, deliveryAddress: string, paymentMethod: string): Observable<Order> {
    const params = new URLSearchParams({ fulfilmentMethod, deliveryAddress, paymentMethod });
    return this.http.post<Order>(`${this.apiUrl}/checkout?${params}`, {});
  }

  myOrders(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/my`);
  }
}