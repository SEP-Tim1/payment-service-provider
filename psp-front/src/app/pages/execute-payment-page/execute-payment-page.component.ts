import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';

@Component({
  selector: 'app-execute-payment-page',
  templateUrl: './execute-payment-page.component.html',
  styleUrls: ['./execute-payment-page.component.css']
})
export class ExecutePaymentPageComponent implements OnInit {

  constructor(
    private paypalService: PaypalService,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) { }

  private paymentId: string = '';
  private payerId: string = '';

  ngOnInit(): void {
    this.route.queryParams
      .subscribe(params => {
        this.paymentId = params['paymentId'];
        this.payerId = params['PayerID'];
      }
    );
  }

  execute() {
    this.paypalService.execute(this.paymentId, this.payerId).subscribe(
      redirectUrl => {
        window.location.href = redirectUrl;
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
