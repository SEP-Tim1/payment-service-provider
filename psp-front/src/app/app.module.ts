import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegistrationPageComponent } from './pages/registration-page/registration-page.component';
import { AuthFormComponent } from './components/auth-form/auth-form.component';

import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatTableModule } from '@angular/material/table';
import { StorePageComponent } from './pages/store-page/store-page.component';
import { CreateStoreComponent } from './components/create-store/create-store.component';
import { StoreInfoComponent } from './components/store-info/store-info.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CustomerPaymentMethodsPageComponent } from './pages/customer-payment-methods-page/customer-payment-methods-page.component';
import { SubscriptionsPageComponent } from './pages/subscriptions-page/subscriptions-page.component';
import { MerchantInfoPageComponent } from './pages/merchant-info-page/merchant-info-page.component';
import { BitcoinSubscriptionComponent } from './pages/bitcoin-subscription/bitcoin-subscription.component';
import { PaypalSubscriptionComponent } from './pages/paypal-subscription/paypal-subscription.component';
import { ExecutePaymentPageComponent } from './pages/execute-payment-page/execute-payment-page.component';
import { ExecuteAgreementPageComponent } from './pages/execute-agreement-page/execute-agreement-page.component';
import { CancelPaymentPageComponent } from './pages/cancel-payment-page/cancel-payment-page.component';
import { CancelAgreementPageComponent } from './pages/cancel-agreement-page/cancel-agreement-page.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    RegistrationPageComponent,
    AuthFormComponent,
    StorePageComponent,
    CreateStoreComponent,
    StoreInfoComponent,
    CustomerPaymentMethodsPageComponent,
    SubscriptionsPageComponent,
    MerchantInfoPageComponent,
    BitcoinSubscriptionComponent,
    PaypalSubscriptionComponent,
    ExecutePaymentPageComponent,
    ExecuteAgreementPageComponent,
    CancelPaymentPageComponent,
    CancelAgreementPageComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MatSnackBarModule,
    MatCardModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatSelectModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatDividerModule,
    MatListModule,
    MatGridListModule,
    MatTableModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
