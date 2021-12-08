import { Component, Input, OnInit } from '@angular/core';
import { Store } from 'src/app/model/store';

@Component({
  selector: 'app-store-info',
  templateUrl: './store-info.component.html',
  styleUrls: ['./store-info.component.css']
})
export class StoreInfoComponent implements OnInit {

  constructor() { }

  @Input() store: Store = new Store(1, '', 1, '');

  ngOnInit(): void {
  }

}
