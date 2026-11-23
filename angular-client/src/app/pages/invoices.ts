import { Component, OnInit, inject, signal } from '@angular/core';
import { ApiService, Customer, Invoice, Product } from '../api.service';

@Component({
  selector: 'app-invoices',
  template: `
    <h3>Factures</h3>
    @if (error()) { <div class="alert alert-danger">{{ error() }}</div> }

    <div class="card mb-4"><div class="card-body">
      <h6>Nouvelle facture</h6>
      <div class="row g-2">
        <div class="col">
          <select #cust class="form-select">
            @for (c of customers(); track c.id) { <option [value]="c.id">{{ c.name }}</option> }
          </select>
        </div>
        <div class="col">
          <select #prod class="form-select">
            @for (p of products(); track p.id) { <option [value]="p.id">{{ p.name }} ({{ p.price }})</option> }
          </select>
        </div>
        <div class="col"><input #qty type="number" value="1" min="1" class="form-control"></div>
        <div class="col-auto"><button class="btn btn-primary" (click)="create(cust, prod, qty)">Créer</button></div>
      </div>
    </div></div>

    @for (i of invoices(); track i.id) {
      <div class="card mb-3">
        <div class="card-header">Facture n°{{ i.id }} — {{ i.invoiceDate }} — Client : {{ i.customer?.name }}</div>
        <div class="card-body p-0">
          <table class="table mb-0">
            <thead><tr><th>Produit</th><th>Prix unitaire</th><th>Quantité</th></tr></thead>
            <tbody>
              @for (it of i.productItems; track it.id) {
                <tr><td>{{ it.product?.name }}</td><td>{{ it.unitPrice }}</td><td>{{ it.quantity }}</td></tr>
              }
            </tbody>
          </table>
        </div>
        <div class="card-footer text-end"><strong>Total : {{ i.total }}</strong></div>
      </div>
    }`
})
export class Invoices implements OnInit {
  private api = inject(ApiService);
  invoices = signal<Invoice[]>([]);
  customers = signal<Customer[]>([]);
  products = signal<Product[]>([]);
  error = signal('');

  ngOnInit() {
    this.api.getCustomers().subscribe(d => this.customers.set(d));
    this.api.getProducts().subscribe(d => this.products.set(d));
    this.load();
  }
  load() {
    this.api.getInvoices().subscribe({
      next: d => { this.invoices.set(d); this.error.set(''); },
      error: () => this.error.set('Impossible de joindre billing-service via la gateway (port 8888).')
    });
  }
  create(cust: HTMLSelectElement, prod: HTMLSelectElement, qty: HTMLInputElement) {
    if (!cust.value || !prod.value) return;
    this.api.addInvoice(+cust.value, [{ productId: +prod.value, quantity: +qty.value || 1 }])
      .subscribe({ next: () => this.load(), error: () => this.error.set('Création de la facture impossible.') });
  }
}
