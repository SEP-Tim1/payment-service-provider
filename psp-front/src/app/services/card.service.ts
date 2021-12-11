import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Card } from '../model/card';

@Injectable({
  providedIn: 'root',
})
export class CardService {
  storeCardPaymentUrl = environment.backend + '/card/card/';

  constructor(private router: Router, private _http: HttpClient) {}

  public isCardPayementEnabled(requestId: number): Observable<Card> {
    return this._http.get<Card>(this.storeCardPaymentUrl + requestId);
  }
}
