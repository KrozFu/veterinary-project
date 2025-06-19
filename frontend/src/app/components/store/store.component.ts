import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  category: string;
  imageUrl: string;
  rating: number;
  stock: number;
}

@Component({
  selector: 'app-store',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './store.component.html',
  styleUrl: './store.component.css',
})
export class StoreComponent {
  activeFilter = signal<string>('all');
  searchQuery = signal<string>('');

  products: Product[] = [
    {
      id: 1,
      name: 'Alimento Premium para Perro',
      description:
        'Bolsa 5kg. Nutrición completa para perros adultos con ingredientes naturales.',
      price: 120000,
      category: 'dog',
      imageUrl:
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVei7esiwphNME67G88ngMRLAXFGVUiChosw&s',
      rating: 4.5,
      stock: 15,
    },
    {
      id: 2,
      name: 'Rascador de Lujo para Gato',
      description:
        'Estructura resistente con múltiples niveles y áreas para descanso.',
      price: 70000,
      category: 'cat',
      imageUrl:
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRo76-vgYOehByOJV7UbaG9axo7kmPThObn5A&s',
      rating: 4.8,
      stock: 8,
    },
    {
      id: 3,
      name: 'Jaula Premium para Hámster',
      description:
        'Espaciosa jaula con tubos, rueda de ejercicio y área de descanso.',
      price: 95000,
      category: 'hamster',
      imageUrl:
        'https://http2.mlstatic.com/D_NQ_NP_867981-MLA72120568425_102023-O.webp',
      rating: 4.2,
      stock: 5,
    },
    {
      id: 4,
      name: 'Acuario 20L con Filtro',
      description:
        'Kit completo con iluminación LED, filtro silencioso y decoración.',
      price: 150000,
      category: 'fish',
      imageUrl:
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6m_GIkwEuniFRIL6J6Kkx999Ne14nnr_mVw&s',
      rating: 4.7,
      stock: 3,
    },
    {
      id: 5,
      name: 'Juguete Interactivo para Perro',
      description: 'Diseñado para estimulación mental, resistente a mordidas.',
      price: 45000,
      category: 'dog',
      imageUrl:
        'https://img.kwcdn.com/product/fancy/c9d01524-ef32-4588-85a7-5dddaed7f0f9.jpg?imageMogr2/auto-orient%7CimageView2/2/w/800/q/70/format/webp',
      rating: 4.3,
      stock: 12,
    },
    {
      id: 6,
      name: 'Arena Aglomerante para Gato',
      description: 'Control de olores, fácil limpieza, paquete 10kg.',
      price: 55000,
      category: 'cat',
      imageUrl:
        'https://puppiscolombia.vteximg.com.br/arquivos/ids/192614-505-505/2-ARENA-PARA-GATOS-10.jpg?v=638525906952970000',
      rating: 4.6,
      stock: 20,
    },
    {
      id: 7,
      name: 'Comida Nutritiva para Peces',
      description: 'Alimento balanceado para peces tropicales, envase 500g.',
      price: 35000,
      category: 'fish',
      imageUrl:
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRr2YRHSEuHzA4cuQsp3QK0YLKW6jZY2xPBQA&s',
      rating: 4.4,
      stock: 18,
    },
    {
      id: 8,
      name: 'Rueda de Ejercicio Silenciosa',
      description: 'Para hámsters y roedores pequeños, base antideslizante.',
      price: 40000,
      category: 'hamster',
      imageUrl:
        'https://http2.mlstatic.com/D_NQ_NP_725514-MCO76237525442_052024-O.webp',
      rating: 4.1,
      stock: 7,
    },
  ];

  filteredProducts = signal<Product[]>(this.products);

  updateFilter(category: string): void {
    this.activeFilter.set(category);
    this.applyFilters();
  }

  onSearchChange(query: string): void {
    this.searchQuery.set(query);
    this.applyFilters();
  }

  applyFilters(): void {
    let filtered = this.products;

    if (this.activeFilter() !== 'all') {
      filtered = filtered.filter(
        (product) => product.category === this.activeFilter()
      );
    }

    if (this.searchQuery()) {
      const query = this.searchQuery().toLowerCase();
      filtered = filtered.filter(
        (product) =>
          product.name.toLowerCase().includes(query) ||
          product.description.toLowerCase().includes(query)
      );
    }

    this.filteredProducts.set(filtered);
  }

  formatPrice(price: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      maximumFractionDigits: 0,
    }).format(price);
  }
}
