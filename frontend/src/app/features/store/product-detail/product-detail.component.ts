import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../../core/product.service';
import { AuthService } from '../../../core/auth.service';
import { CartService } from '../../../core/cart.service';
import { Product, Variant } from '../../../models/product.model';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {

  product?: Product;
  selected?: Variant;
  loading = true;
  adding = false;
  added = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private productService: ProductService,
              private auth: AuthService,
              private cart: CartService) {}

  ngOnInit() {
    const slug = this.route.snapshot.paramMap.get('slug')!;
    this.productService.getBySlug(slug).subscribe({
      next: (p) => {
        this.product = p;
        this.selected = p.variants[0];
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  selectColor(colorName: string) {
    this.selected = this.product?.variants.find(v => v.colorName === colorName);
    this.added = false;
  }

  selectStorage(storage: string) {
    const match = this.product?.variants.find(
      v => v.storage === storage && v.colorName === this.selected?.colorName);
    if (match) { this.selected = match; this.added = false; }
  }

  // ⭐ ADD TO CART!
  addToCart() {
    if (!this.selected) return;

    // Login නැත්නම් → login page එකට
    if (!this.auth.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }

    this.adding = true;
    this.cart.addItem(this.selected.sku, 1).subscribe({
      next: () => { this.adding = false; this.added = true; },
      error: (err) => {
        this.adding = false;
        alert(err.error?.message || 'Could not add to cart');
      }
    });
  }

  get uniqueColors(): Variant[] {
    const seen = new Set<string>();
    return (this.product?.variants ?? []).filter(v =>
      !seen.has(v.colorName) && seen.add(v.colorName));
  }

  get storagesForColor(): string[] {
    return (this.product?.variants ?? [])
      .filter(v => v.colorName === this.selected?.colorName)
      .map(v => v.storage);
  }

  get specEntries(): [string, any][] {
    return Object.entries(this.product?.specs ?? {});
  }
}