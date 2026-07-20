import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';

export interface AuthResponse {
  token: string;
  userId: string;
  fullName: string;
  email: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private apiUrl = 'http://localhost:8081/api/v1/auth';

  // ⭐ Signals — login state එක app එක පුරාම live!
  currentUser = signal<AuthResponse | null>(this.loadUser());

  constructor(private http: HttpClient) {}

  register(data: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data)
      .pipe(tap(res => this.saveUser(res)));
  }

  login(data: { email: string; password: string }): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, data)
      .pipe(tap(res => this.saveUser(res)));
  }

  logout() {
    localStorage.removeItem('istore_user');
    this.currentUser.set(null);
  }

  get token(): string | null {
    return this.currentUser()?.token ?? null;
  }

  get isLoggedIn(): boolean {
    return !!this.token;
  }

  private saveUser(res: AuthResponse) {
    localStorage.setItem('istore_user', JSON.stringify(res));
    this.currentUser.set(res);
  }

  private loadUser(): AuthResponse | null {
    const raw = localStorage.getItem('istore_user');
    return raw ? JSON.parse(raw) : null;
  }
}