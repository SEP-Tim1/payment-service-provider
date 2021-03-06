import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
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
    private snackBar: MatSnackBar,
    private router: Router
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
        this.router.navigate(['']);
      }
    )
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'Okay', {
      duration: 5000,
    });
  }

}
