import { Component, OnInit, HostListener, ElementRef, ViewChild } from '@angular/core';
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

  // ⭐ Category → hero video + title + optional height/width/fit/noVideo/rotate mapping
  heroConfig: Record<string, { video: string; title: string; accent: string; sub: string; height?: string; width?: string; fit?: string; noVideo?: boolean; rotate?: boolean }> = {
    '':            { video: 'hero-all.mp4',         title: 'iPhone 17',   accent: 'Pro Max', sub: 'Heat-forged aluminium. Unibody design. So Pro.' },
    'iPhone':      { video: 'hero-iphone.mp4',      title: 'iPhone 17',   accent: 'Pro Max', sub: 'The most powerful iPhone ever.' },
    'Mac':         { video: 'hero-mac.mp4',         title: 'MacBook',     accent: 'Pro',     sub: 'Mind-blowing. Head-turning.' },
    'iPad':        { video: 'hero-ipad.mp4',        title: 'iPad',        accent: 'Pro',     sub: 'Thinpossible.', height: '200vh', fit: 'cover' },
    'Watch':       { video: 'hero-watch.mp4',       title: 'Watch',       accent: 'Ultra',   sub: 'The ultimate sports watch.', height: '90vh', fit: 'cover' },
    'AirPods':     { video: 'hero-airpods.mp4',     title: 'AirPods',     accent: 'Pro',     sub: 'Adaptive Audio. Now playing.' },
    'Accessories': { video: '',                     title: 'Accessories', accent: '',        sub: 'Perfect companions for your Apple gear.', noVideo: true }
  };

  // ⭐ AirPods 3D scroll-rotate state
  @ViewChild('rotateSection') rotateSection?: ElementRef;
  rotateAngle = 0;       // continuous rotation (720° = 2 full spins)
  rotateScale = 0.85;
  scrollProgress = 0;    // 0 → 1 (raw progress — text timing එකට)
  scaleX = 1;            // ⭐ edge-on "thin" illusion එකට

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

  // ⭐ Scroll listener — AirPods rotate section එකේ progress track කරනවා
  @HostListener('window:scroll')
  onScroll() {
    if (!this.rotateSection) return;

    const el = this.rotateSection.nativeElement as HTMLElement;
    const rect = el.getBoundingClientRect();
    const sectionHeight = el.offsetHeight - window.innerHeight;

    if (sectionHeight <= 0) return;

    const scrolled = Math.min(Math.max(-rect.top, 0), sectionHeight);
    const progress = scrolled / sectionHeight;
    this.scrollProgress = progress;

    // ⭐ Continuous rotation — එකම direction එකෙන් දිගටම!
    this.rotateAngle = progress * 720;   // full rotations 2ක්

    // ⭐ 90°/270° ලඟදී "thin" (narrow) විදිහට පේන්න scaleX අඩු කරනවා — edge illusion!
    const normalizedAngle = ((this.rotateAngle % 360) + 360) % 360;
    const distTo90 = Math.abs(normalizedAngle - 90);
    const distTo270 = Math.abs(normalizedAngle - 270);
    const minDist = Math.min(distTo90, distTo270);

    this.scaleX = minDist < 30 ? Math.max(0.15, minDist / 30) : 1;
    this.rotateScale = 0.85 + (Math.sin(progress * Math.PI) * 0.2);
  }

  // Text sections 3ක් — scrollProgress (raw 0→1) එක අනුව fade in/out
  textOpacity(index: number): number {
    const sectionProgress = this.scrollProgress * 3;
    const distance = Math.abs(sectionProgress - index - 0.5);
    return Math.max(0, 1 - distance * 2);
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