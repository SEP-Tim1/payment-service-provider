import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Card } from '../model/card';
import { Merchant } from '../model/merchant';
import { Enabled } from '../model/enabled';
import { InvoiceResponse } from '../model/invoiceResponse';

@Injectable({
  providedIn: 'root',
})
export class CardService {
  storeCardPaymentUrl = environment.backend + '/card/card/';
  isCardPayementEnabledForStoreUrl = environment.backend + '/card/card/store/';
  enableUrl = environment.backend + '/card/card/enable/';
  disableUrl = environment.backend + '/card/card/disable/';
  invoiceUrl = environment.backend + '/card/card/invoice/';

  constructor(private router: Router, private _http: HttpClient) {}

  public isCardPayementEnabled(requestId: number): Observable<Card> {
    return this._http.get<Card>(this.storeCardPaymentUrl + requestId);
  }

  public isCardPayementEnabledForStore(storeId: number): Observable<Card> {
    return this._http.get<Card>(
      this.isCardPayementEnabledForStoreUrl + storeId
    );
  }

  public enableCardPayment(
    dto: Merchant,
    storeId: String
  ): Observable<Enabled> {
    return this._http.post<Enabled>(this.enableUrl + storeId, dto);
  }

  public disableCardPayment(storeId: number): Observable<Enabled> {
    return this._http.get<Enabled>(this.disableUrl + storeId);
  }

  public getInvoiceResponse(requestId: number): Observable<InvoiceResponse> {
    return this._http.get<InvoiceResponse>(this.invoiceUrl + requestId);
  }
}
