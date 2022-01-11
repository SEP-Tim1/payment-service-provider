import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BitcoinService {

  private subBaseUrl = environment.backend + '/bitcoin/sub/';
  private paymentBaseUrl = environment.backend + '/bitcoin/payment/';

  constructor(private _http: HttpClient) { }

  createSubscription(apiKey: string): Observable<any> {
    return this._http.post(this.subBaseUrl + apiKey, null);
  }

  deleteSubscription(): Observable<any> {
    return this._http.delete(this.subBaseUrl);
  }

  subscribed(storeId: number): Observable<boolean> {
    return this._http.get<boolean>(this.subBaseUrl + storeId);
  }

  isEnabledForRequest(requestId: number): Observable<boolean> {
    return this._http.get<boolean>(this.paymentBaseUrl + requestId);
  }
}
