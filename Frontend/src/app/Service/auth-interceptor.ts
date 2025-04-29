import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const customInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  const localToken = localStorage.getItem('token');
  const excludedEndpoints = ['loginUser', 'registerUser']; // Endpoints to exclude

  // Check if the request URL matches any excluded endpoint
  for (const endpoint of excludedEndpoints) {
    if (req.url.includes(endpoint)) {
      return next(req);
    }
  }

  // Function to check if token is expired
  const isTokenExpired = (token: string): boolean => {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiry = payload.exp;
      const now = Math.floor(Date.now() / 1000);
      return expiry < now;
    } catch (e) {
      console.error('Error decoding token', e);
      return true; // If error in decoding, treat as expired
    }
  };

  if (localToken) {
    if (isTokenExpired(localToken)) {
      console.warn('Token expired. Redirecting to login.');
      router.navigate(['/login']);
      return next(req); // Optionally, you can also block the request here
    }

    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${localToken}`,
      },
    });
    return next(clonedReq);
  }

  return next(req);
};
