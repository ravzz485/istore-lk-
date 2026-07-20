import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductService } from '../../../core/product.service';
import { Product } from '../../../models/product.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {

  products: Product[] = [];
  loading = true;
  error = '';
  activeCategory = '';

  // ⭐ Category → hero video + title + optional height/width/fit mapping
  heroConfig: Record<string, { video: string; title: string; accent: string; sub: string; height?: string; width?: string; fit?: string }> = {
    '':            { video: 'hero-all.mp4',         title: 'iPhone 17',   accent: 'Pro Max', sub: 'Heat-forged aluminium. Unibody design. So Pro.' },
    'iPhone':      { video: 'hero-iphone.mp4',      title: 'iPhone 17',   accent: 'Pro Max', sub: 'The most powerful iPhone ever.' },
    'Mac':         { video: 'hero-mac.mp4',         title: 'MacBook',     accent: 'Pro',     sub: 'Mind-blowing. Head-turning.' },
    'iPad':        { video: 'hero-ipad.mp4',        title: 'iPad',        accent: 'Pro',     sub: 'Thinpossible.', height: '75vh' },
    'Watch':       { video: 'hero-watch.mp4',       title: 'Watch',       accent: 'Ultra',   sub: 'The ultimate sports watch.', height: '90vh', fit: 'cover' },
    'AirPods':     { video: 'hero-airpods.mp4',     title: 'AirPods',     accent: 'Pro',     sub: 'Adaptive Audio. Now playing.' },
    'Accessories': { video: 'hero-accessories.mp4', title: 'Accessories', accent: '',        sub: 'Perfect companions for your Apple gear.' }
  };

  constructor(private productService: ProductService,
              private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.activeCategory = params['category'] ?? '';
      this.loadProducts(this.activeCategory || undefined);
    });
  }

  get hero() {
    return this.heroConfig[this.activeCategory] ?? this.heroConfig[''];
  }

  // Video fail වුනොත් (file එක නැත්නම්) → default video එකට fallback
  onVideoError(ev: Event) {
    const video = ev.target as HTMLVideoElement;
    const fallback = 'hero-all.mp4';
    if (!video.src.endsWith(fallback)) {
      video.src = fallback;
      video.load(); video.play();
    }
  }

  loadProducts(category?: string) {
    this.loading = true;
    this.productService.getProducts(category).subscribe({
      next: (page) => { this.products = page.content; this.loading = false; },
      error: () => { this.error = 'Could not load products.'; this.loading = false; }
    });
  }

  minPrice(p: Product): number {
    return Math.min(...p.variants.map(v => v.price));
  }
}