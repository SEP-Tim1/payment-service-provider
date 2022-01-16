import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BitcoinSubscriptionComponent } from './pages/bitcoin-subscription/bitcoin-subscription.component';
import { CancelAgreementPageComponent } from './pages/cancel-agreement-page/cancel-agreement-page.component';
import { CancelPaymentPageComponent } from './pages/cancel-payment-page/cancel-payment-page.component';
import { CustomerPaymentMethodsPageComponent } from './pages/customer-payment-methods-page/customer-payment-methods-page.component';
import { ExecuteAgreementPageComponent } from './pages/execute-agreement-page/execute-agreement-page.component';
import { ExecutePaymentPageComponent } from './pages/execute-payment-page/execute-payment-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { MerchantInfoPageComponent } from './pages/merchant-info-page/merchant-info-page.component';
import { PaypalSubscriptionComponent } from './pages/paypal-subscription/paypal-subscription.component';
import { RegistrationPageComponent } from './pages/registration-page/registration-page.component';
import { StorePageComponent } from './pages/store-page/store-page.component';
import { SubscriptionsPageComponent } from './pages/subscriptions-page/subscriptions-page.component';

const routes: Routes = [
  { path: '', component: LoginPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'registration', component: RegistrationPageComponent },
  {
    path: 'subscriptions',
    component: SubscriptionsPageComponent,
    data: { expextedRoles: ['MERCHANT'] },
  },
  {
    path: 'merchant',
    component: MerchantInfoPageComponent,
    data: { expextedRoles: ['MERCHANT'] },
  },
  {
    path: 'bitcoin',
    component: BitcoinSubscriptionComponent,
    data: { expextedRoles: ['MERCHANT'] },
  },
  {
    path: 'paypal',
    component: PaypalSubscriptionComponent,
    data: { expextedRoles: ['MERCHANT'] },
  },
  {
    path: 'store',
    component: StorePageComponent,
    data: { expextedRoles: ['MERCHANT'] },
  },
  { path: 'payment/:id', component: CustomerPaymentMethodsPageComponent },
  { path: 'paypal/execute/sub/:id', component: ExecuteAgreementPageComponent },
  { path: 'paypal/cancel/sub/:id', component: CancelAgreementPageComponent },
  { path: 'paypal/execute', component: ExecutePaymentPageComponent },
  { path: 'paypal/cancel/:id', component: CancelPaymentPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
