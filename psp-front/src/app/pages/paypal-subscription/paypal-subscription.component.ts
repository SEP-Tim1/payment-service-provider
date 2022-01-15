import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { PayPalMSub } from 'src/app/model/paypal-msub';
import { PaypalService } from 'src/app/services/paypal.service';

@Component({
  selector: 'app-paypal-subscription',
  templateUrl: './paypal-subscription.component.html',
  styleUrls: ['./paypal-subscription.component.css']
})
export class PaypalSubscriptionComponent implements OnInit {

  constructor(
    private service: PaypalService, 
    private snackBar: MatSnackBar,
    private router: Router) { }

  public clientId: string = '';
  public clientSecret: string = '';

  ngOnInit(): void {
  }

  sendRequest() {
    const sub: PayPalMSub = new PayPalMSub(this.clientId, this.clientSecret);
    this.service.createSub(sub).subscribe(
      _ => {
        this.openSnackBar('PayPal payments are enabled.');
          this.router.navigate(['subscriptions']);
      },
      error => {
        this.openSnackBar(error.error);
      }
    )
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'Okay', {
      duration: 5000,
    });
  }

}
