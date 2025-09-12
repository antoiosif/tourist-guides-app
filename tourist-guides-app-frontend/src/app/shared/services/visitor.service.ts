import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment.development';
import { VisitorInsertDTO, VisitorReadOnlyDTO, VisitorUpdateDTO } from '../interfaces/visitor';
import { VisitorFilters } from '../interfaces/filters';
import { Paginated } from '../interfaces/paginated';

const API_URL = `${environment.apiURL}/api/visitors`

@Injectable({
  providedIn: 'root'
})
export class VisitorService {
  http: HttpClient = inject(HttpClient);
  
  insertVisitor(visitorInsertDTO: VisitorInsertDTO) {
    return this.http.post<VisitorReadOnlyDTO>(`${API_URL}`, visitorInsertDTO);
  }

  updateVisitor(uuid: string, visitorUpdateDTO: VisitorUpdateDTO) {
    return this.http.put<VisitorReadOnlyDTO>(`${API_URL}/${uuid}`, visitorUpdateDTO);
  }

  deleteVisitor(uuid: string) {
    return this.http.delete<void>(`${API_URL}/${uuid}`);
  }

  getVisitorByUuid(uuid: string) {
    return this.http.get<VisitorReadOnlyDTO>(`${API_URL}/${uuid}`);
  }

  getVisitorsFilteredSorted(filters: VisitorFilters | null) {
    return this.http.post<VisitorReadOnlyDTO[]>(`${API_URL}/search`, filters);
  }

  getVisitorsPaginatedFilteredSorted(filters: VisitorFilters | null) {
    return this.http.post<Paginated<VisitorReadOnlyDTO>>(`${API_URL}/search/paginated`, filters);
  }

  isUsernameExists(username: string) {
    return this.http.get<Boolean>(`${API_URL}/check-username-exists/${username}`);
  }
}
