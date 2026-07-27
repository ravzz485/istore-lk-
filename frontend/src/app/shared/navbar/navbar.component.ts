import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';
import { CartService } from '../../core/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit {

  categories = ['iPhone', 'Mac', 'iPad', 'Watch', 'AirPods', 'Accessories'];
  activeCategory = '';

  // ⭐ Cart item count — live signal (badge එකට!)
  cartCount = this.cart.itemCount;

  constructor(private router: Router,
              private route: ActivatedRoute,
              public auth: AuthService,
              private cart: CartService) {}

  // ⭐ URL එකෙන් category එක කියවනවා — click එකෙන් නෙවෙයි.
  //    ඒක නිසා promo tile එකකින් ආවත් pill එක හරියට update වෙනවා.
  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.activeCategory = params['category'] ?? '';
    });
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}