import { Component, OnInit, AfterViewInit, HostListener, ElementRef, ViewChild } from '@angular/core';
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
export class HomeComponent implements OnInit, AfterViewInit {

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
  rotateAngle = 0;
  rotateScale = 0.85;
  scrollProgress = 0;
  scaleX = 1;

  // ⭐ iPad "Take a closer look" — arrow scroll
  @ViewChild('closerScroll') closerScroll?: ElementRef;

  // ⭐ Mac "Get to know" — arrow scroll
  @ViewChild('getknowScroll') getknowScroll?: ElementRef;

  // ⭐ iPhone "Get to know" — arrow scroll
  @ViewChild('iphoneScroll') iphoneScroll?: ElementRef;

  // ⭐ iPhone "Take a closer look" — interactive color + features
  activeFeature = 'colors';        // colors | ceramic | immersive | camera
  activeColor = 'orange';          // orange | blue | white

  // ⭐ "All in the family" — scroll tilt + word-by-word reveal
  @ViewChild('familyImg') familyImg?: ElementRef;
  @ViewChild('familyText') familyText?: ElementRef;

  familyTilt = 14;                 // deg — rotateX (start value)
  familyScale = 0.92;
  familyOpacities: number[] = [];
  familyWords: { t: string; bold: boolean; sup: string }[] = [];

  // *word = bold,  ^13 = superscript
  private familySource =
    'The latest iPhone models come packed with advanced capabilities. ' +
    'Helpful Apple Intelligence features such as visual intelligence^13 and ' +
    'Writing Tools to make your everyday easier. Fast, secure connections ' +
    'with *Wi-Fi *7,^14 *Bluetooth *6, *5G *connectivity,^15 *and *eSIM.^16 And safety ' +
    'features like Messages via satellite^17 designed to give you peace of mind.';

  constructor(private productService: ProductService,
              private route: ActivatedRoute) {}

  ngOnInit() {
    // ⭐ Family word list එක build කරනවා (once only)
    this.familyWords = this.familySource.split(' ').map(w => {
      const bold = w.startsWith('*');
      let t = bold ? w.slice(1) : w;
      const m = t.match(/\^(\d+)$/);
      const sup = m ? m[1] : '';
      if (m) t = t.replace(/\^\d+$/, '');
      return { t, bold, sup };
    });
    this.familyOpacities = this.familyWords.map(() => 0.28);

    this.route.queryParams.subscribe(params => {
      this.activeCategory = params['category'] ?? '';
      this.loadProducts(this.activeCategory || undefined);
    });
  }

  // ⭐ Page එක scroll වෙච්ච තැනක load වුනොත් — initial state එක හරි ගස්සනවා
  ngAfterViewInit() {
    setTimeout(() => this.updateFamily(), 0);
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

  // ⭐ Scroll listener — AirPods rotate + Family animation
  @HostListener('window:scroll')
  onScroll() {
    this.updateFamily();          // ⭐ මුලින්ම — පහළ return එකට උඩින්!

    if (!this.rotateSection) return;

    const el = this.rotateSection.nativeElement as HTMLElement;
    const rect = el.getBoundingClientRect();
    const sectionHeight = el.offsetHeight - window.innerHeight;

    if (sectionHeight <= 0) return;

    const scrolled = Math.min(Math.max(-rect.top, 0), sectionHeight);
    const progress = scrolled / sectionHeight;
    this.scrollProgress = progress;

    this.rotateAngle = progress * 720;

    const normalizedAngle = ((this.rotateAngle % 360) + 360) % 360;
    const distTo90 = Math.abs(normalizedAngle - 90);
    const distTo270 = Math.abs(normalizedAngle - 270);
    const minDist = Math.min(distTo90, distTo270);

    this.scaleX = minDist < 30 ? Math.max(0.15, minDist / 30) : 1;
    this.rotateScale = 0.85 + (Math.sin(progress * Math.PI) * 0.2);
  }

  // ⭐ Element එකක viewport progress එක 0 → 1
  private progressOf(el: HTMLElement, startPct = 0.9, endPct = 0.45): number {
    const rect = el.getBoundingClientRect();
    const vh = window.innerHeight;
    const startY = vh * startPct;
    const endY = vh * endPct;
    const travel = startY - endY + rect.height;
    if (travel <= 0) return 0;
    return Math.min(Math.max((startY - rect.top) / travel, 0), 1);
  }

  // ⭐ "All in the family" — image tilt + text word reveal
  private updateFamily() {
    // 1️⃣ Image — tilt back → upright
    if (this.familyImg) {
      const p = this.progressOf(this.familyImg.nativeElement, 0.95, 0.55);
      const eased = 1 - Math.pow(1 - p, 3);          // ease-out cubic
      this.familyTilt = 14 * (1 - eased);
      this.familyScale = 0.92 + eased * 0.08;
    }

    // 2️⃣ Text — වචනෙන් වචනෙ light up
    if (this.familyText) {
      const p = this.progressOf(this.familyText.nativeElement, 0.9, 0.45);
      const n = this.familyWords.length;
      if (!n) return;
      const per = 1 / n;
      for (let i = 0; i < n; i++) {
        const d = (p - i * per) / (per * 7);          // 7 words wide fade band
        this.familyOpacities[i] = 0.28 + Math.min(Math.max(d, 0), 1) * 0.72;
      }
    }
  }

  // Text sections 3ක් — scrollProgress (raw 0→1) එක අනුව fade in/out
  textOpacity(index: number): number {
    const sectionProgress = this.scrollProgress * 3;
    const distance = Math.abs(sectionProgress - index - 0.5);
    return Math.max(0, 1 - distance * 2);
  }

  // ⭐ iPad "Take a closer look" — arrow scroll
  scrollCloser(direction: number) {
    if (!this.closerScroll) return;
    const el = this.closerScroll.nativeElement as HTMLElement;
    const scrollAmount = el.clientWidth * 0.6;
    el.scrollBy({ left: scrollAmount * direction, behavior: 'smooth' });
  }

  // ⭐ Mac "Get to know" — arrow scroll
  scrollGetknow(direction: number) {
    if (!this.getknowScroll) return;
    const el = this.getknowScroll.nativeElement as HTMLElement;
    el.scrollBy({ left: el.clientWidth * 0.5 * direction, behavior: 'smooth' });
  }

  // ⭐ iPhone "Get to know" — arrow scroll
  scrollIphone(direction: number) {
    if (!this.iphoneScroll) return;
    const el = this.iphoneScroll.nativeElement as HTMLElement;
    el.scrollBy({ left: el.clientWidth * 0.5 * direction, behavior: 'smooth' });
  }

  // ⭐ iPhone "Take a closer look" — interactive
  get activeColorName(): string {
    return { orange: 'Cosmic Orange', blue: 'Deep Blue', white: 'Silver' }[this.activeColor] || '';
  }

  get activeColorHex(): string {
    return { orange: '#e8663d', blue: '#2e3f5c', white: '#f0f0f0' }[this.activeColor] || '#e8663d';
  }

  get closerImage(): string {
    if (this.activeFeature === 'colors') {
      return `iphone-${this.activeColor}.jpg`;
    }
    return `${this.activeFeature}.jpg`;   // ceramic.jpg / immersive.jpg / camera.jpg
  }

  selectFeature(feature: string) {
    this.activeFeature = feature;
  }

  setColor(color: string) {
    this.activeColor = color;
    this.activeFeature = 'colors';
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