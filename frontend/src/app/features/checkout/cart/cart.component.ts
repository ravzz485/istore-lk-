import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Cart, CartService } from '../../../core/cart.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent implements OnInit {

  cart?: Cart;
  loading = true;

  constructor(private cartService: CartService) {}

  ngOnInit() { this.load(); }

  load() {
    this.cartService.getCart().subscribe({
      next: (c) => { this.cart = c; this.loading = false; },
      error: () => this.loading = false
    });
  }

  remove(sku: string) {
    this.cartService.removeItem(sku).subscribe(c => this.cart = c);
  }

  get total(): number {
    return (this.cart?.items ?? [])
      .reduce((sum, i) => sum + i.unitPrice * i.qty, 0);
  }
}