import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ReportService {

  private apiUrl = 'http://localhost:8081/api/v1/reports';

  constructor(private http: HttpClient) {}

  dashboard(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/dashboard`);
  }

  monthlyRevenue(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/revenue/monthly`);
  }

  salesByCategory(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/sales/by-category`);
  }
}