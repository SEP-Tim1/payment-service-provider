import { Component, OnInit } from '@angular/core';
import { Store } from 'src/app/model/store';
import { StoreService } from 'src/app/services/store.service';

@Component({
  selector: 'app-store-page',
  templateUrl: './store-page.component.html',
  styleUrls: ['./store-page.component.css']
})
export class StorePageComponent implements OnInit {

  constructor(private service: StoreService) { }

  store: Store | null = null;

  ngOnInit(): void {
    this.get();
  }

  get() {
    this.service.get().subscribe(
      result => this.store = result
    )
  }
}
