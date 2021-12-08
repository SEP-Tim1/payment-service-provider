import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';
import { User } from '../model/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  loginUrl = environment.backend + '/auth/auth/login';
  registrerUrl = environment.backend + '/auth/auth/register';

  helper = new JwtHelperService();
  loginComponent = false;
  registerComponent = false;

  constructor(private router: Router, private _http: HttpClient) {}

  logIn(user: User): Observable<string> {
    return this._http.post(this.loginUrl, user, { responseType: 'text' });
  }

  register(user: User): Observable<any> {
    return this._http.post(this.registrerUrl, user);
  }

  isLoggedIn() {
    if (localStorage.getItem('token') != null && !this.isExpired()) return true;
    else return false;
  }

  isExpired() {
    let decoded = this.decodeToken();
    if (Date.now() < decoded.exp * 1000) {
      return false;
    }
    return true;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRole() {
    if (this.isLoggedIn()) return this.decodeToken().role;
    return '';
  }

  getId() {
    if (this.isLoggedIn()) return this.decodeToken().id;
    return '';
  }

  getUsername() {
    if (this.isLoggedIn()) return this.decodeToken().sub;
    return '';
  }

  decodeToken() {
    let token = localStorage.getItem('token');
    if (token) return this.helper.decodeToken(token);
  }

  setToken(token: string) {
    localStorage.setItem('token', token);
    localStorage.setItem('role', this.getRole());
    localStorage.setItem('id', this.getId());
    localStorage.setItem('username', this.getUsername());
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('id');
    localStorage.removeItem('username');
    this.router.navigate(['login']);
  }
}
