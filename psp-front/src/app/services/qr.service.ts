import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { Card } from '../model/card';
import { Merchant } from '../model/merchant';
import { Enabled } from '../model/enabled';
import { InvoiceResponse } from '../model/invoiceResponse';
import { QRCode } from '../model/qrCode';
import { QRCodeEnabled } from '../model/qrCodeEnabled';

@Injectable({
  providedIn: 'root',
})
export class QRService {
  storeQRPaymentUrl = environment.backend + '/qrcode/qr/';
  isQRPayementEnabledForStoreUrl = environment.backend + '/qrcode/qr/store/';
  enableUrl = environment.backend + '/qrcode/qr/enable/';
  disableUrl = environment.backend + '/qrcode/qr/disable/';
  invoiceUrl = environment.backend + '/qrcode/qr/invoice/';
  generateQRUrl = environment.backend + '/qrcode/qr/code/';

  constructor(private _http: HttpClient) {}

  public isQRPayementEnabled(requestId: number): Observable<QRCodeEnabled> {
    return this._http.get<QRCodeEnabled>(this.storeQRPaymentUrl + requestId);
  }

  public isQRPayementEnabledForStore(
    storeId: number
  ): Observable<QRCodeEnabled> {
    return this._http.get<QRCodeEnabled>(
      this.isQRPayementEnabledForStoreUrl + storeId
    );
  }

  public enable(dto: Merchant, storeId: String): Observable<Enabled> {
    return this._http.post<Enabled>(this.enableUrl + storeId, dto);
  }

  public disable(storeId: number): Observable<Enabled> {
    return this._http.get<Enabled>(this.disableUrl + storeId);
  }

  public getInvoiceResponse(requestId: number): Observable<InvoiceResponse> {
    return this._http.get<InvoiceResponse>(this.invoiceUrl + requestId);
  }

  public getPaymentQRCode(requestId: number): Observable<QRCode> {
    return this._http.get<QRCode>(this.generateQRUrl + requestId);
  }
}
