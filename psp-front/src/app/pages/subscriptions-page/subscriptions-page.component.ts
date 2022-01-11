import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { BitcoinService } from 'src/app/services/bitcoin.service';
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
    private bitcoinService: BitcoinService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.storeId = Number(localStorage.getItem('storeId'));
    this.checkAvailablePaymentMethods();
  }

  checkAvailablePaymentMethods() {
    this.cardPaymentEnabled();
    this.bitcoinPaymentEnabled();
  }

  cardPaymentEnabled() {
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

  bitcoinPaymentEnabled() {
    this.bitcoinService.subscribed(this.storeId).subscribe(
      subscribed => {
        this.bitcoin = subscribed;
      }
    )
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

  bitcoinPayment() {
    if (!this.bitcoin) {
      this.router.navigate(['bitcoin']);
    } else {
      this.bitcoinService.deleteSubscription().subscribe(
        _ => {
          this.bitcoin = false;
          this.openSnackBar("Bitcoin payments are disabled");
        },
        error => {
          this.openSnackBar(error.error);
        }
      )
    }
  }

  paypalToggle() {
    this.paypal = !this.paypal;
  }

  qrcodeToggle() {
    this.qrcode = !this.qrcode;
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'Okay', {
      duration: 5000,
    });
  }
}
