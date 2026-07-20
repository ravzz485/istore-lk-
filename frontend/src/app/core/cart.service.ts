import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';

export interface CartItem {
  sku: string;
  productId: string;
  productName: string;
  qty: number;
  unitPrice: number;
}

export interface Cart {
  id: string;
  customerId: string;
  items: CartItem[];
}

@Injectable({ providedIn: 'root' })
export class CartService {

  private apiUrl = 'http://localhost:8081/api/v1/cart';

  // ⭐ Navbar badge එකට live item count!
  itemCount = signal(0);

  constructor(private http: HttpClient) {}

  getCart(): Observable<Cart> {
    return this.http.get<Cart>(this.apiUrl)
      .pipe(tap(cart => this.updateCount(cart)));
  }

  addItem(sku: string, qty = 1): Observable<Cart> {
    return this.http.post<Cart>(`${this.apiUrl}/items?sku=${sku}&qty=${qty}`, {})
      .pipe(tap(cart => this.updateCount(cart)));
  }

  removeItem(sku: string): Observable<Cart> {
    return this.http.delete<Cart>(`${this.apiUrl}/items/${sku}`)
      .pipe(tap(cart => this.updateCount(cart)));
  }

  private updateCount(cart: Cart) {
    this.itemCount.set(cart.items.reduce((sum, i) => sum + i.qty, 0));
  }
}