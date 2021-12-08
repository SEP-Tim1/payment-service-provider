import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'psp-front';

  constructor(public authService: AuthService, private router: Router) {}

  home() {
    let route = '';
    if (this.authService.isLoggedIn()) {
      route = 'store';
    }
    this.router.navigate([route]);
  }
}
