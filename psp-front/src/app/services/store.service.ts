import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Store } from '../model/store';

@Injectable({
  providedIn: 'root'
})
export class StoreService {

  storeUrl = environment.backend + '/store/store';

  constructor(private _http: HttpClient) { }

  public create(name: string): Observable<any> {
    return this._http.post(this.storeUrl + '/' + name, null);
  }

  public get(): Observable<Store> {
    return this._http.get<Store>(this.storeUrl);
  }
}
