import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment.development';
import { ActivityInsertDTO, ActivityReadOnlyDTO, ActivityUpdateDTO } from '../interfaces/activity';
import { ActivityFilters } from '../interfaces/filters';
import { Paginated } from '../interfaces/paginated';

const API_URL = `${environment.apiURL}/api/activities`

@Injectable({
  providedIn: 'root'
})
export class ActivityService {
  http: HttpClient = inject(HttpClient);

  insertActivity(activityInsertDTO: ActivityInsertDTO) {
    return this.http.post<ActivityReadOnlyDTO>(`${API_URL}`, activityInsertDTO);
  }

  updateActivity(uuid: string, activityUpdateDTO: ActivityUpdateDTO) {
    return this.http.put<ActivityReadOnlyDTO>(`${API_URL}/${uuid}`, activityUpdateDTO);
  }

  deleteActivity(uuid: string) {
    return this.http.delete<string>(`${API_URL}/${uuid}`);
  }

  getActivityByUuid(uuid: string) {
    return this.http.get<ActivityReadOnlyDTO>(`${API_URL}/${uuid}`);
  }

  getActivitiesFilteredSorted(filters: ActivityFilters | null) {
    return this.http.post<ActivityReadOnlyDTO[]>(`${API_URL}/search`, filters);
  }

  getActivitiesPaginatedFilteredSorted(filters: ActivityFilters | null) {
    return this.http.post<Paginated<ActivityReadOnlyDTO>>(`${API_URL}/search/paginated`, filters);
  }
}
