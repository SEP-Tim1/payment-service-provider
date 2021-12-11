import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerPaymentMethodsPageComponent } from './pages/customer-payment-methods-page/customer-payment-methods-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { MerchantInfoPageComponent } from './pages/merchant-info-page/merchant-info-page.component';
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
    path: 'store',
    component: StorePageComponent,
    data: { expextedRoles: ['MERCHANT'] },
  },
  { path: 'payment/:id', component: CustomerPaymentMethodsPageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
