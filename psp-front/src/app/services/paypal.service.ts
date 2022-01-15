import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PayPalMSub } from '../model/paypal-msub';

@Injectable({
  providedIn: 'root'
})
export class PaypalService {

  private subBaseUrl = environment.backend + '/paypal/merchant/sub/'

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
}
