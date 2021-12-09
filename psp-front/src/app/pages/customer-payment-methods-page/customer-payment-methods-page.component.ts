import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PaymentRequestService } from 'src/app/services/payment-request.service';

@Component({
  selector: 'app-customer-payment-methods-page',
  templateUrl: './customer-payment-methods-page.component.html',
  styleUrls: ['./customer-payment-methods-page.component.css']
})
export class CustomerPaymentMethodsPageComponent implements OnInit {

  constructor(private route: ActivatedRoute, private service: PaymentRequestService) { }

  requestId = -1;
  card = false;
  qrcode = false;
  paypal = false;
  bitcoin = false;

  ngOnInit(): void {
    this.requestId = this.getRequestId();
    this.checkAvailablePaymentMethods();
  }

  getRequestId(): number {
    return Number(this.route.snapshot.paramMap.get('id'));
  }

  checkAvailablePaymentMethods() {
    //pozvati svaki payment servis i proveriti da li taj request id podrzava taj nacin placanja
    //u zavisnosti od toga postaviti flagove
    //ako nijedan nacin placanja nije podrzan, customer se redirektuje na failure url koji se nalazi u request-u
    if(!this.card && !this.qrcode && !this.paypal && !this.bitcoin) {
      this.service.getFailureUrl(this.requestId).subscribe(
        redirectUrl => {console.log(redirectUrl);  window.location.href = redirectUrl;}
      )
    }
  }
}
