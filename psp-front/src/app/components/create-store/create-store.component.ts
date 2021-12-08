import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { StoreService } from 'src/app/services/store.service';

@Component({
  selector: 'app-create-store',
  templateUrl: './create-store.component.html',
  styleUrls: ['./create-store.component.css']
})
export class CreateStoreComponent implements OnInit {

  constructor(private service: StoreService, private snackBar: MatSnackBar) { }

  namee: string = '';
  @Output() storeCreated = new EventEmitter<boolean>();

  ngOnInit(): void {
  }

  create() {
    this.service.create(this.namee).subscribe(
      _ => this.storeCreated.emit(true),
      error => this.openSnackBar(error.error)
    )
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, "Okay", {
      duration: 5000,
    });
  }
}
