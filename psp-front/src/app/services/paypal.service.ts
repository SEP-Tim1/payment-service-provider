import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PayPalMSub } from '../model/paypal-msub';

@Injectable({
  providedIn: 'root'
})
export class PaypalService {

  private subBaseUrl = environment.backend + '/paypal/merchant/sub/';
  private payBaseUrl = environment.backend + '/paypal/payment/';

  constructor(private _http: HttpClient) { }

  createSub(sub: PayPalMSub): Observable<any> {
    return this._http.post(this.subBaseUrl, sub);
  }

  deleteSub(): Observable<any> {
    return this._http.delete(this.subBaseUrl);
  }

  isSub(): Observable<boolean> {
    return this._http.get<boolean>(this.subBaseUrl);
  }

  isEnabled(requestId: number): Observable<boolean> {
    return this._http.get<boolean>(this.payBaseUrl + requestId);
  }

  pay(requestId: number): Observable<string> {
    return this._http.post(this.payBaseUrl + requestId, null, { responseType: 'text' });
  }

  execute(paymentId: string, payerId: string): Observable<string> {
    return this._http.post(this.payBaseUrl + 'execute/' + paymentId + '/' + payerId, null, { responseType: 'text' });
  }

  cancel(requestId: string): Observable<string> {
    return this._http.put(this.payBaseUrl + 'cancel/' + requestId, null, { responseType: 'text' });
  }

  executeAgreement(requestId: string, token: string): Observable<string> {
    return this._http.post(this.payBaseUrl + 'execute/sub/' + requestId + '/' + token, null, { responseType: 'text' });
  }

  cancelAgreement(requestId: string): Observable<string> {
    return this._http.put(this.payBaseUrl + 'cancel/sub/' + requestId, null, { responseType: 'text' });
  }
}
