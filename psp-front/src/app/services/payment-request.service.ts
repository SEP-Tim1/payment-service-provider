import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PaymentRequestService {

  private getFailureUrlUrl = environment.backend + '/request/request/failure';

  constructor(private _http: HttpClient) { }

  getFailureUrl(requestId: number): Observable<string> {
    return this._http.get(this.getFailureUrlUrl + '/' + requestId, {responseType: 'text'});
  }
}
