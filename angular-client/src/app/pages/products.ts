import { Component, OnInit, inject, signal } from '@angular/core';
import { ApiService, Product } from '../api.service';

@Component({
  selector: 'app-products',
  template: `
    <h3>Produits</h3>
    <div class="row g-2 mb-3">
      <div class="col"><input #name class="form-control" placeholder="Nom"></div>
      <div class="col"><input #price type="number" class="form-control" placeholder="Prix"></div>
      <div class="col"><input #qty type="number" class="form-control" placeholder="Quantité"></div>
      <div class="col-auto"><button class="btn btn-primary" (click)="add(name, price, qty)">Ajouter</button></div>
    </div>
    @if (error()) { <div class="alert alert-danger">{{ error() }}</div> }
    <table class="table table-striped">
      <thead><tr><th>ID</th><th>Nom</th><th>Prix</th><th>Quantité</th><th></th></tr></thead>
      <tbody>
        @for (p of products(); track p.id) {
          <tr><td>{{ p.id }}</td><td>{{ p.name }}</td><td>{{ p.price }}</td><td>{{ p.quantity }}</td>
            <td><button class="btn btn-sm btn-outline-danger" (click)="remove(p.id!)">Supprimer</button></td></tr>
        }
      </tbody>
    </table>`
})
export class Products implements OnInit {
  private api = inject(ApiService);
  products = signal<Product[]>([]);
  error = signal('');

  ngOnInit() { this.load(); }
  load() {
    this.api.getProducts().subscribe({
      next: d => { this.products.set(d); this.error.set(''); },
      error: () => this.error.set('Impossible de joindre inventory-service via la gateway (port 8888).')
    });
  }
  add(name: HTMLInputElement, price: HTMLInputElement, qty: HTMLInputElement) {
    if (!name.value.trim()) return;
    this.api.addProduct({ name: name.value, price: +price.value, quantity: +qty.value }).subscribe(() => {
      name.value = ''; price.value = ''; qty.value = ''; this.load();
    });
  }
  remove(id: number) { this.api.deleteProduct(id).subscribe(() => this.load()); }
}
