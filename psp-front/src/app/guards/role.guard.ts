import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const expectedRoles: string[] = route.data['expectedRoles'];
      if (
        !expectedRoles.includes(this.authService.getRole()) &&
        this.authService.isLoggedIn()
      ) {
        this.authService.logout();
        this.router.navigate(['login']);
        return false;
      }
      if (!this.authService.isLoggedIn() && expectedRoles.length > 0) {
        this.authService.logout();
        this.router.navigate(['login']);
        return false;
      }
      return true;
  }
  
}
