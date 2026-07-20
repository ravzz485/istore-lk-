export interface Variant {
  sku: string;
  colorName: string;
  colorHex: string;
  storage: string;
  condition: string;
  price: number;
  stock: number;
  lowStockThreshold: number;
  images: string[];
}

export interface Product {
  id: string;
  name: string;
  slug: string;
  category: string;
  modelNumber: string;
  releaseYear: number;
  description: string;
  status: string;
  specs: Record<string, any>;
  variants: Variant[];
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
}