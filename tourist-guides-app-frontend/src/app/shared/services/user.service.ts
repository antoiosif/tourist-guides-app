import { Injectable, effect, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { environment } from 'src/environments/environment.development';
import { AuthenticationResponseDTO, Credentials, LoggedInUser } from '../interfaces/user';

const API_URL = `${environment.apiURL}/api/auth`

@Injectable({
  providedIn: 'root'
})
export class UserService {
  http: HttpClient = inject(HttpClient);
  router: Router = inject(Router);
  user$ = signal<LoggedInUser | null>(null);

  constructor() {
    // Repopulate 'user$' with data from the token we have stored in the local storage
    // (for when the user Refreshes the page)
    const access_token = localStorage.getItem('access_token');
    if (access_token) {
      const decodedTokenSubject = jwtDecode(access_token) as unknown as LoggedInUser;
      this.user$.set({
        sub: decodedTokenSubject.sub,
        uuid: decodedTokenSubject.uuid,
        firstname: decodedTokenSubject.firstname,
        lastname: decodedTokenSubject.lastname,
        role: decodedTokenSubject.role
      });
    }

    effect(() => {
      if (this.user$()) {
        console.log('User Logged In: ', this.user$()?.sub);
      } else {
        console.log('No user Logged In');
      }
    })
  }

  loginUser(credentials: Credentials) {
    return this.http.post<AuthenticationResponseDTO>(`${API_URL}/authenticate`, credentials);
  }

  logoutUser(): void {
    this.user$.set(null);
    localStorage.removeItem('access_token');
    this.router.navigate(['']);
  }

  isTokenExpired(): boolean {
    const token = localStorage.getItem('access_token');

    if (!token) return true;
    try {
      const decoded = jwtDecode(token);
      const exp = decoded.exp;
      const now = Math.floor(Date.now()/1000);
      if (exp) {
        return exp < now;
      } else {
        return true;
      }
    } catch (err) {
      return true;
    }
  }
}
