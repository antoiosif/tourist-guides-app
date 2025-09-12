import { Injectable, effect, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment.development';
import { LanguageInsertDTO, LanguageReadOnlyDTO, LanguageUpdateDTO } from '../interfaces/language';

const API_URL = `${environment.apiURL}/api/languages`

@Injectable({
  providedIn: 'root'
})
export class LanguageService {
  http: HttpClient = inject(HttpClient);
  languages$ = signal<LanguageReadOnlyDTO[]>([]);
  languagesSorted$ = signal<LanguageReadOnlyDTO[]>([]);

  constructor() {
    this.getAllLanguages()
      .subscribe((response) => {
        this.languages$.set(response);
      })

    this.getAllLanguagesSortedByName()
      .subscribe((response) => {
        this.languagesSorted$.set(response);
      })

    effect(() => {
      if (this.languages$()) {
        console.log('Get all languages were returned successfully');
      }

      if (this.languagesSorted$()) {
        console.log('Get all languages sorted were returned successfully');
      }
    })
  }

  insertLanguage(languageInsertDTO: LanguageInsertDTO) {
    return this.http.post<LanguageReadOnlyDTO>(`${API_URL}`, languageInsertDTO);
  }

  updateLanguage(id: bigint, languageUpdateDTO: LanguageUpdateDTO) {
    return this.http.put<LanguageReadOnlyDTO>(`${API_URL}/${id}`, languageUpdateDTO);
  }

  deleteLanguage(id: bigint) {
    return this.http.delete<void>(`${API_URL}/${id}`);
  }

  getLanguageById(id: bigint) {
    return this.http.get<LanguageReadOnlyDTO>(`${API_URL}/${id}`);
  }

  getLanguageByName(name: string) {
    return this.http.get<LanguageReadOnlyDTO>(`${API_URL}/name/${name}`);
  }

  getAllLanguages() {
    return this.http.get<LanguageReadOnlyDTO[]>(`${API_URL}`);
  }

  getAllLanguagesSortedByName() {
    return this.http.get<LanguageReadOnlyDTO[]>(`${API_URL}/sorted`);
  }

  isCodeExists(code: string) {
    return this.http.get<boolean>(`${API_URL}/check-code-exists/${code}`);
  }

  isNameExists(name: string) {
    return this.http.get<boolean>(`${API_URL}/check-name-exists/${name}`);
  }
}
