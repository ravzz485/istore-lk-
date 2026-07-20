import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../core/product.service';
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
  selected?: Variant;          // ⭐ දැනට select වුනු variant එක
  loading = true;

  constructor(private route: ActivatedRoute,
              private productService: ProductService) {}

  ngOnInit() {
    const slug = this.route.snapshot.paramMap.get('slug')!;
    this.productService.getBySlug(slug).subscribe({
      next: (p) => {
        this.product = p;
        this.selected = p.variants[0];   // default: පළවෙනි variant
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  // Color dot click — same color වල පළවෙනි variant එක select
  selectColor(colorName: string) {
    this.selected = this.product?.variants.find(v => v.colorName === colorName);
  }

  // Storage click — selected color + ඒ storage
  selectStorage(storage: string) {
    const match = this.product?.variants.find(
      v => v.storage === storage && v.colorName === this.selected?.colorName);
    if (match) this.selected = match;
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