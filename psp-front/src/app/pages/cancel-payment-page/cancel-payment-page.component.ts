import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';

@Component({
  selector: 'app-cancel-payment-page',
  templateUrl: './cancel-payment-page.component.html',
  styleUrls: ['./cancel-payment-page.component.css']
})
export class CancelPaymentPageComponent implements OnInit {

  constructor(
    private paypalService: PaypalService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) { }

  private requestId: string = '';

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.requestId = id;
    }
    this.execute();
  }

  execute() {
    if (this.requestId === '') {
      this.router.navigate(['']);
    }
    this.paypalService.cancel(this.requestId).subscribe(
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
