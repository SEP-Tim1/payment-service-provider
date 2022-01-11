import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { InvoiceResponse } from 'src/app/model/invoiceResponse';
import { BitcoinService } from 'src/app/services/bitcoin.service';
import { CardService } from 'src/app/services/card.service';
import { PaymentRequestService } from 'src/app/services/payment-request.service';

@Component({
  selector: 'app-customer-payment-methods-page',
  templateUrl: './customer-payment-methods-page.component.html',
  styleUrls: ['./customer-payment-methods-page.component.css'],
})
export class CustomerPaymentMethodsPageComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private service: PaymentRequestService,
    private bitcoinService: BitcoinService,
    private cardService: CardService,
    private snackBar: MatSnackBar
  ) {}

  requestId = -1;
  card = false;
  qrcode = false;
  paypal = false;
  bitcoin = false;

  invoiceResponse = new InvoiceResponse('', 0);

  ngOnInit(): void {
    this.requestId = this.getRequestId();
    this.checkAvailablePaymentMethods();
  }

  getRequestId(): number {
    return Number(this.route.snapshot.paramMap.get('id'));
  }

  bla() {}

  cardPayment() {
    this.cardService.getInvoiceResponse(this.requestId).subscribe(
      (response) => {
        this.invoiceResponse = response;
        this.openSnackBar('Payment URL and ID are generated.');
        console.log(response);
        window.location.href = response.paymentUrl.toString();
      },
      (error) => {
        console.log(error.error);
        this.openSnackBar(error.error);
      }
    );
  }

  bitcoinPayment() {
    this.bitcoinService.createCharge(this.requestId).subscribe(
      redirectUrl => {
        window.location.href = redirectUrl;
      },
      error => {
        console.log(error);
      }
    );
  }

  checkAvailablePaymentMethods() {
    this.checkCard();
  }

  checkCard() {
    this.cardService.isCardPayementEnabled(this.requestId).subscribe(
      (response) => {
        this.card = response.cardPaymentEnabled;
        this.checkBitcoin();
      },
      _ => {
        this.checkBitcoin();
      }
    );
  }

  checkBitcoin() {
    this.bitcoinService.isEnabledForRequest(this.requestId).subscribe(
      enabled => {
        this.bitcoin = enabled;
        this.checkAll();
      }
    )
  }

  checkAll() {
    if (!this.card && !this.qrcode && !this.paypal && !this.bitcoin) {
      this.service.getFailureUrl(this.requestId).subscribe((redirectUrl) => {
        console.log(redirectUrl);
        window.location.href = redirectUrl;
      });
    }
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'Okay', {
      duration: 5000,
    });
  }
}
