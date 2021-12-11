import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from 'src/app/model/store';

@Component({
  selector: 'app-store-info',
  templateUrl: './store-info.component.html',
  styleUrls: ['./store-info.component.css'],
})
export class StoreInfoComponent implements OnInit {
  constructor(private router: Router) {}

  @Input() store: Store = new Store(1, '', 1, '');

  ngOnInit(): void {}

  subscriptionsPage() {
    localStorage.setItem('storeId', this.store.id.toString());
    this.router.navigate(['subscriptions']);
  }
}
