import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerPaymentMethodsPageComponent } from './pages/customer-payment-methods-page/customer-payment-methods-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegistrationPageComponent } from './pages/registration-page/registration-page.component';
import { StorePageComponent } from './pages/store-page/store-page.component';

const routes: Routes = [
  { path: '', component: LoginPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'registration', component: RegistrationPageComponent },
  { path: 'store', component: StorePageComponent, data: {expextedRoles: ['MERCHANT']}},
  { path: 'payment/:id', component: CustomerPaymentMethodsPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
