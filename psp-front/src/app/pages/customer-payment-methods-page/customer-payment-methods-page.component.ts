import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { InvoiceResponse } from 'src/app/model/invoiceResponse';
import { BitcoinService } from 'src/app/services/bitcoin.service';
import { CardService } from 'src/app/services/card.service';
import { PaymentRequestService } from 'src/app/services/payment-request.service';
import { PaypalService } from 'src/app/services/paypal.service';
import { QRService } from 'src/app/services/qr.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-customer-payment-methods-page',
  templateUrl: './customer-payment-methods-page.component.html',
  styleUrls: ['./customer-payment-methods-page.component.css'],
})
export class CustomerPaymentMethodsPageComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: PaymentRequestService,
    private bitcoinService: BitcoinService,
    private cardService: CardService,
    private qrService: QRService,
    private paypalService: PaypalService,
    private snackBar: MatSnackBar,
    private domSanitizer: DomSanitizer
  ) {}

  requestId = -1;
  card = false;
  qrcode = false;
  paypal = false;
  bitcoin = false;
  image = false;
  imageurl: SafeResourceUrl = '';

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
        console.log(response);
        window.location.href = response.paymentUrl.toString();
      },
      (error) => {
        console.log(error.error);
        this.openSnackBar(error.error);
      }
    );
  }

  qrcodePayment() {
    this.qrService.getInvoiceResponse(this.requestId).subscribe(
      (response) => {
        this.invoiceResponse = response;
        localStorage.setItem('paymentLink', response.paymentUrl.toString());
        console.log(response);
        this.qrService.getPaymentQRCode(this.requestId).subscribe((qrcode) => {
          localStorage.setItem('qrcode', qrcode.url);
          console.log(qrcode.url);
          this.imageurl =
            'https://localhost:8090/qrcode/qr/image/' + qrcode.url;
          this.image = true;
        });
      },
      (error) => {
        console.log(error.error);
        this.openSnackBar(error.error);
      }
    );
  }

  bitcoinPayment() {
    this.bitcoinService.createCharge(this.requestId).subscribe(
      (redirectUrl) => {
        window.location.href = redirectUrl;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  paypalPayment() {
    this.paypalService.pay(this.requestId).subscribe(
      (redirectUrl) => {
        window.location.href = redirectUrl;
      },
      (error) => {
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
      (_) => {
        this.checkBitcoin();
      }
    );
  }

  checkBitcoin() {
    this.bitcoinService.isEnabledForRequest(this.requestId).subscribe(
      (enabled) => {
        this.bitcoin = enabled;
        this.checkPayPal();
      },
      (_) => {
        this.checkPayPal();
      }
    );
  }

  checkPayPal() {
    this.paypalService.isEnabled(this.requestId).subscribe(
      (enabled) => {
        this.paypal = enabled;

        this.checkQRCode();
      },
      (_) => {
        this.checkQRCode();
      }
    );
  }

  checkQRCode() {
    this.qrService.isQRPayementEnabled(this.requestId).subscribe(
      (enabled) => {
        this.qrcode = enabled.qrPaymentEnabled;
        this.checkAll();
      },
      (_) => {
        this.checkAll();
      }
    );
  }

  checkAll() {
    if (!this.card && !this.qrcode && !this.paypal && !this.bitcoin) {
      this.service.getFailureUrl(this.requestId).subscribe(
        (redirectUrl) => {
          console.log(redirectUrl);
          window.location.href = redirectUrl;
        },
        (_) => {
          this.router.navigate(['']);
        }
      );
    }
  }

  showMessage() {
    this.openSnackBar(
      'Payment has not been carried out! Please try again, or proceed with another payment method.'
    );
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'Okay', {
      duration: 5000,
    });
  }
}
