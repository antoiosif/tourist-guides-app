import { Injectable, effect, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment.development';
import { CategoryInsertDTO, CategoryReadOnlyDTO, CategoryUpdateDTO } from '../interfaces/category';

const API_URL = `${environment.apiURL}/api/categories`

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  http: HttpClient = inject(HttpClient);
  categories$ = signal<CategoryReadOnlyDTO[]>([]);
  categoriesSorted$ = signal<CategoryReadOnlyDTO[]>([]);

  constructor() {
    this.getAllCategories()
      .subscribe((response) => {
        this.categories$.set(response);
      })

    this.getAllCategoriesSortedByName()
      .subscribe((response) => {
        this.categoriesSorted$.set(response);
      })

    effect(() => {
      if (this.categories$()) {
        console.log('Get all categories were returned successfully');
      }

      if (this.categoriesSorted$()) {
        console.log('Get all categories sorted were returned successfully');
      }
    })
  }

  insertCategory(categoryInsertDTO: CategoryInsertDTO) {
    return this.http.post<CategoryReadOnlyDTO>(`${API_URL}`, categoryInsertDTO);
  }

  updateCategory(id: bigint, categoryUpdateDTO: CategoryUpdateDTO) {
    return this.http.put<CategoryReadOnlyDTO>(`${API_URL}/${id}`, categoryUpdateDTO);
  }

  deleteCategory(id: bigint) {
    return this.http.delete<void>(`${API_URL}/${id}`);
  }

  getCategoryById(id: bigint) {
    return this.http.get<CategoryReadOnlyDTO>(`${API_URL}/${id}`);
  }

  getCategoryByName(name: string) {
    return this.http.get<CategoryReadOnlyDTO>(`${API_URL}/name/${name}`);
  }

  getAllCategories() {
    return this.http.get<CategoryReadOnlyDTO[]>(`${API_URL}`);
  }

  getAllCategoriesSortedByName() {
    return this.http.get<CategoryReadOnlyDTO[]>(`${API_URL}/sorted`);
  }

  isNameExists(name: string) {
    return this.http.get<boolean>(`${API_URL}/check-name-exists/${name}`);
  }
}
