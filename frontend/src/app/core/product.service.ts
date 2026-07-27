import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page, Product } from '../models/product.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProductService {

  // ⭐ localhost hardcode නෑ — environment එකෙන් එනවා
  private apiUrl = `${environment.apiUrl}/api/v1/products`;

  constructor(private http: HttpClient) {}

  getProducts(category?: string): Observable<Page<Product>> {
    const url = category ? `${this.apiUrl}?category=${category}` : this.apiUrl;
    return this.http.get<Page<Product>>(url);
  }

  getBySlug(slug: string): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${slug}`);
  }
}