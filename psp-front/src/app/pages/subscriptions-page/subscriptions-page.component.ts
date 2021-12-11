import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { CardService } from 'src/app/services/card.service';

@Component({
  selector: 'app-subscriptions-page',
  templateUrl: './subscriptions-page.component.html',
  styleUrls: ['./subscriptions-page.component.css'],
})
export class SubscriptionsPageComponent implements OnInit {
  card = false;
  qrcode = false;
  paypal = false;
  bitcoin = false;
  storeId = 0;

  constructor(
    private cardService: CardService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    //obrisati kada podaci za store card payment budu uneti u bazu
    this.paypal = true;
    this.storeId = Number(localStorage.getItem('storeId'));
    this.checkAvailablePaymentMethods();
  }

  checkAvailablePaymentMethods() {
    this.cardService.isCardPayementEnabledForStore(this.storeId).subscribe(
      (response) => {
        console.log(response);
        this.card = response.cardPaymentEnabled;
      },
      (error) => {
        console.log(error.error);
      }
    );
  }

  cardPayment() {
    if (!this.card) {
      this.router.navigate(['merchant']);
    } else {
      this.cardService.disableCardPayment(this.storeId).subscribe(
        (response) => {
          console.log(response);
          this.card = false;
          this.openSnackBar('Card payment is disabled.');
        },
        (error) => {
          this.openSnackBar(error.error);
        }
      );
    }
  }

  paypalToggle() {
    this.paypal = !this.paypal;
  }

  qrcodeToggle() {
    this.qrcode = !this.qrcode;
  }

  bitcoinToggle() {
    this.bitcoin = !this.bitcoin;
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'Okay', {
      duration: 5000,
    });
  }
}
