import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth.service';

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

  constructor(private router: Router, public auth: AuthService) {}

  selectCategory(cat: string) {
    this.activeCategory = cat;
    this.router.navigate(['/'], { queryParams: cat ? { category: cat } : {} });
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}