import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';
import { CartService } from '../../core/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

  categories = ['iPhone', 'Mac', 'iPad', 'Watch', 'AirPods', 'Accessories'];
  activeCategory = '';

  // ⭐ Cart item count — live signal (badge එකට!)
  cartCount = this.cart.itemCount;

  constructor(private router: Router,
              public auth: AuthService,
              private cart: CartService) {}

  selectCategory(cat: string) {
    this.activeCategory = cat;
    this.router.navigate(['/'], { queryParams: cat ? { category: cat } : {} });
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}