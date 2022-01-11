import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { BitcoinService } from 'src/app/services/bitcoin.service';

@Component({
  selector: 'app-bitcoin-subscription',
  templateUrl: './bitcoin-subscription.component.html',
  styleUrls: ['./bitcoin-subscription.component.css']
})
export class BitcoinSubscriptionComponent implements OnInit {

  constructor(
    private bitcoinService: BitcoinService,
    private snackBar: MatSnackBar,
    private router: Router) { }

  public apiKey: string = '';

  ngOnInit(): void {
  }

  sendRequest() {
    this.bitcoinService.createSubscription(this.apiKey).subscribe(
      _ => {
        this.openSnackBar('Bitcoin payments are enabled.');
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
