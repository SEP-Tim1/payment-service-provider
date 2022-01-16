import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { PaypalService } from 'src/app/services/paypal.service';

@Component({
  selector: 'app-execute-agreement-page',
  templateUrl: './execute-agreement-page.component.html',
  styleUrls: ['./execute-agreement-page.component.css']
})
export class ExecuteAgreementPageComponent implements OnInit {

  constructor(
    private paypalService: PaypalService,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private router: Router
  ) { }

  private requestId: string = '';
  private token: string = '';

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.requestId = id;
    }
    this.route.queryParams
      .subscribe(params => {
        this.token = params['token'];
      }
    );
  }

  execute() {
    this.paypalService.executeAgreement(this.requestId, this.token).subscribe(
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
