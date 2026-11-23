import { Routes } from '@angular/router';
import { Customers } from './pages/customers';
import { Products } from './pages/products';
import { Invoices } from './pages/invoices';

export const routes: Routes = [
  { path: 'customers', component: Customers },
  { path: 'products', component: Products },
  { path: 'invoices', component: Invoices },
  { path: '', redirectTo: 'customers', pathMatch: 'full' }
];
