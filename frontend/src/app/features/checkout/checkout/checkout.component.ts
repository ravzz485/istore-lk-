import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Cart, CartService } from '../../../core/cart.service';
import { Order, OrderService } from '../../../core/order.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.scss'
})
export class CheckoutComponent implements OnInit {

  cart?: Cart;
  loading = true;
  placing = false;
  errorMsg = '';
  placedOrder?: Order;      // ⭐ order success → confirmation view!

  form = this.fb.group({
    fulfilmentMethod: ['DELIVERY', Validators.required],
    deliveryAddress: ['', Validators.required],
    paymentMethod: ['COD', Validators.required]
  });

  constructor(private fb: FormBuilder,
              private cartService: CartService,
              private orderService: OrderService,
              private router: Router) {}

  ngOnInit() {
    this.cartService.getCart().subscribe({
      next: (c) => {
        this.cart = c;
        this.loading = false;
        if (!c.items.length) this.router.navigate(['/cart']);
      },
      error: () => this.loading = false
    });

    // Pickup select කළාම address ඕන නෑ
    this.form.controls.fulfilmentMethod.valueChanges.subscribe(v => {
      const addr = this.form.controls.deliveryAddress;
      if (v === 'PICKUP') { addr.clearValidators(); addr.setValue('Store Pickup - Colombo'); }
      else { addr.setValidators(Validators.required); addr.setValue(''); }
      addr.updateValueAndValidity();
    });
  }

  get total(): number {
    return (this.cart?.items ?? []).reduce((s, i) => s + i.unitPrice * i.qty, 0);
  }

  placeOrder() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.placing = true;
    this.errorMsg = '';

    const v = this.form.value;
    this.orderService.checkout(v.fulfilmentMethod!, v.deliveryAddress!, v.paymentMethod!).subscribe({
      next: (order) => {
        this.placedOrder = order;              // 🎉 confirmation!
        this.cartService.itemCount.set(0);     // badge reset
        this.placing = false;
      },
      error: (err) => {
        this.errorMsg = err.error?.message || 'Could not place order. Please try again.';
        this.placing = false;
      }
    });
  }
}