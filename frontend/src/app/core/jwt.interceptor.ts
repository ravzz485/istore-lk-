import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

// ⭐ හැම API request එකකම token එක auto-attach!
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  if (auth.token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${auth.token}` }
    });
  }
  return next(req);
};
