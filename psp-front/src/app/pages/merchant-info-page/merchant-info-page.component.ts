import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Merchant } from 'src/app/model/merchant';
import { CardService } from 'src/app/services/card.service';

@Component({
  selector: 'app-merchant-info-page',
  templateUrl: './merchant-info-page.component.html',
  styleUrls: ['./merchant-info-page.component.css'],
})
export class MerchantInfoPageComponent implements OnInit {
  dto = new Merchant(0, '', 0);
  mid = '';
  idControl = new FormControl('', [
    Validators.required,
    Validators.pattern('[0-9]*'),
  ]);
  constructor(
    private cardService: CardService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {}

  sendRequest() {
    this.dto.mid = Number(this.mid);
    var storeId = localStorage.getItem('storeId');
    if (storeId) {
      this.cardService.enableCardPayment(this.dto, storeId).subscribe(
        (response) => {
          console.log(response);
          this.openSnackBar('Card Payment is enabled.');
          this.router.navigate(['subscriptions']);
        },
        (error) => {
          this.openSnackBar(error.error);
        }
      );
    } else {
      this.openSnackBar('Store not found.');
    }
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'Okay', {
      duration: 5000,
    });
  }
}
