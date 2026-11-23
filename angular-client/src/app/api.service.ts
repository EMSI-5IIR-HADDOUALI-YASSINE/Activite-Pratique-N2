import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

// Toutes les requêtes passent par la Gateway (routes dynamiques : /<service-id en minuscules>/...)
export const GATEWAY = 'http://localhost:8888';

export interface Customer { id?: number; name: string; email: string; }
export interface Product { id?: number; name: string; price: number; quantity: number; }
export interface ProductItem { id?: number; productId: number; quantity: number; unitPrice: number; product?: Product; }
export interface Invoice {
  id: number; invoiceDate: string; customerId: number; total: number;
  customer?: Customer; productItems: ProductItem[];
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);

  // Spring Data REST renvoie du HAL : { _embedded: { customers: [...] } }
  getCustomers(): Observable<Customer[]> {
    return this.http.get<any>(`${GATEWAY}/customer-service/customers`).pipe(map(r => r._embedded?.customers ?? []));
  }
  addCustomer(c: Customer) { return this.http.post(`${GATEWAY}/customer-service/customers`, c); }
  deleteCustomer(id: number) { return this.http.delete(`${GATEWAY}/customer-service/customers/${id}`); }

  getProducts(): Observable<Product[]> {
    return this.http.get<any>(`${GATEWAY}/inventory-service/products`).pipe(map(r => r._embedded?.products ?? []));
  }
  addProduct(p: Product) { return this.http.post(`${GATEWAY}/inventory-service/products`, p); }
  deleteProduct(id: number) { return this.http.delete(`${GATEWAY}/inventory-service/products/${id}`); }

  getInvoices(): Observable<Invoice[]> { return this.http.get<Invoice[]>(`${GATEWAY}/billing-service/invoices`); }
  addInvoice(customerId: number, items: { productId: number; quantity: number }[]) {
    return this.http.post<Invoice>(`${GATEWAY}/billing-service/invoices`, { customerId, items });
  }
}
