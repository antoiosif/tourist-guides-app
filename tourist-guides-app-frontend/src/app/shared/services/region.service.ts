import { Injectable, effect, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment.development';
import { RegionInsertDTO, RegionReadOnlyDTO, RegionUpdateDTO } from '../interfaces/region';

const API_URL = `${environment.apiURL}/api/regions`

@Injectable({
  providedIn: 'root'
})
export class RegionService {
  http: HttpClient = inject(HttpClient);
  regions$ = signal<RegionReadOnlyDTO[]>([]);
  regionsSorted$ = signal<RegionReadOnlyDTO[]>([]);

  constructor() {
    this.getAllRegions()
      .subscribe((response) => {
        this.regions$.set(response);
      })

    this.getAllRegionsSortedByName()
      .subscribe((response) => {
        this.regionsSorted$.set(response);
      })

    effect(() => {
      if (this.regions$()) {
        console.log('Get all regions were returned successfully');
      }

      if (this.regionsSorted$()) {
        console.log('Get all regions sorted were returned successfully');
      }
    })
  }

  insertRegion(regionInsertDTO: RegionInsertDTO) {
    return this.http.post<RegionReadOnlyDTO>(`${API_URL}`, regionInsertDTO);
  }

  updateRegion(id: bigint, regionUpdateDTO: RegionUpdateDTO) {
    return this.http.put<RegionReadOnlyDTO>(`${API_URL}/${id}`, regionUpdateDTO);
  }

  deleteRegion(id: bigint) {
    return this.http.delete<void>(`${API_URL}/${id}`);
  }

  getRegionById(id: bigint) {
    return this.http.get<RegionReadOnlyDTO>(`${API_URL}/${id}`);
  }

  getRegionByName(name: string) {
    return this.http.get<RegionReadOnlyDTO>(`${API_URL}/name/${name}`);
  }

  getAllRegions() {
    return this.http.get<RegionReadOnlyDTO[]>(`${API_URL}`);
  }

  getAllRegionsSortedByName() {
    return this.http.get<RegionReadOnlyDTO[]>(`${API_URL}/sorted`);
  }

  isNameExists(name: string) {
    return this.http.get<boolean>(`${API_URL}/check-name-exists/${name}`);
  }
}
