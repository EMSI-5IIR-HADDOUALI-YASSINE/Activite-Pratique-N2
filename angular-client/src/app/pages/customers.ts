import { Component, OnInit, inject, signal } from '@angular/core';
import { ApiService, Customer } from '../api.service';

@Component({
  selector: 'app-customers',
  template: `
    <h3>Clients</h3>
    <div class="row g-2 mb-3">
      <div class="col"><input #name class="form-control" placeholder="Nom"></div>
      <div class="col"><input #email class="form-control" placeholder="Email"></div>
      <div class="col-auto"><button class="btn btn-primary" (click)="add(name, email)">Ajouter</button></div>
    </div>
    @if (error()) { <div class="alert alert-danger">{{ error() }}</div> }
    <table class="table table-striped">
      <thead><tr><th>ID</th><th>Nom</th><th>Email</th><th></th></tr></thead>
      <tbody>
        @for (c of customers(); track c.id) {
          <tr><td>{{ c.id }}</td><td>{{ c.name }}</td><td>{{ c.email }}</td>
            <td><button class="btn btn-sm btn-outline-danger" (click)="remove(c.id!)">Supprimer</button></td></tr>
        }
      </tbody>
    </table>`
})
export class Customers implements OnInit {
  private api = inject(ApiService);
  customers = signal<Customer[]>([]);
  error = signal('');

  ngOnInit() { this.load(); }
  load() {
    this.api.getCustomers().subscribe({
      next: d => { this.customers.set(d); this.error.set(''); },
      error: () => this.error.set('Impossible de joindre customer-service via la gateway (port 8888).')
    });
  }
  add(name: HTMLInputElement, email: HTMLInputElement) {
    if (!name.value.trim()) return;
    this.api.addCustomer({ name: name.value, email: email.value }).subscribe(() => {
      name.value = ''; email.value = ''; this.load();
    });
  }
  remove(id: number) { this.api.deleteCustomer(id).subscribe(() => this.load()); }
}
