import { Routes } from '@angular/router';
import { HomeComponent } from './features/store/home/home.component';
import { ProductDetailComponent } from './features/store/product-detail/product-detail.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { CartComponent } from './features/checkout/cart/cart.component';
import { CheckoutComponent } from './features/checkout/checkout/checkout.component';
import { MyOrdersComponent } from './features/account/my-orders/my-orders.component';
import { DashboardComponent } from './features/admin/dashboard/dashboard.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'products/:slug', component: ProductDetailComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'cart', component: CartComponent },
  { path: 'checkout', component: CheckoutComponent },
  { path: 'my-orders', component: MyOrdersComponent },
  { path: 'admin/dashboard', component: DashboardComponent },
  { path: '**', redirectTo: '' }        // ⭐ wildcard හැමවෙලේම අන්තිමට!
];