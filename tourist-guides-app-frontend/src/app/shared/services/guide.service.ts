import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment.development';
import { GuideInsertDTO, GuideReadOnlyDTO, GuideUpdateDTO } from '../interfaces/guide';
import { GuideFilters } from '../interfaces/filters';
import { Paginated } from '../interfaces/paginated';
import { ActivityReadOnlyDTO } from '../interfaces/activity';

const API_URL = `${environment.apiURL}/api/guides`

@Injectable({
  providedIn: 'root'
})
export class GuideService {
  http: HttpClient = inject(HttpClient);

  insertGuide(guideInsertDTO: GuideInsertDTO) {
    return this.http.post<GuideReadOnlyDTO>(`${API_URL}`, guideInsertDTO);
  }

  updateGuide(uuid: string, guideUpdateDTO: GuideUpdateDTO) {
    return this.http.put<GuideReadOnlyDTO>(`${API_URL}/${uuid}`, guideUpdateDTO);
  }

  deleteGuide(uuid: string) {
    return this.http.delete<void>(`${API_URL}/${uuid}`);
  }

  getGuideByUuid(uuid: string) {
    return this.http.get<GuideReadOnlyDTO>(`${API_URL}/${uuid}`);
  }

  getGuidesFilteredSorted(filters: GuideFilters | null) {
    return this.http.post<GuideReadOnlyDTO[]>(`${API_URL}/search`, filters);
  }

  getGuidesPaginatedFilteredSorted(filters: GuideFilters | null) {
    return this.http.post<Paginated<GuideReadOnlyDTO>>(`${API_URL}/search/paginated`, filters);
  }

  isUsernameExists(username: string) {
    return this.http.get<Boolean>(`${API_URL}/check-username-exists/${username}`);
  }

  isRecordNumberExists(recordNumber: string) {
    return this.http.get<Boolean>(`${API_URL}/check-record-number-exists/${recordNumber}`);
  }

  // for Guide's favorite Activities
  addActivityToGuide(uuid: string | undefined, activityUuid: string) {
    return this.http.put<void>(`${API_URL}/${uuid}/favorites/add`, activityUuid);
  }

  removeActivityFromGuide(uuid: string | undefined, activityUuid: string) {
    return this.http.put<void>(`${API_URL}/${uuid}/favorites/remove`, activityUuid);
  }

  getGuideActivities(uuid: string | undefined) {
    return this.http.get<ActivityReadOnlyDTO[]>(`${API_URL}/${uuid}/favorites`);
  }

  isActivityExistsInGuide(uuid: string | undefined, activityUuid: string) {
    return this.http.post<boolean>(`${API_URL}/${uuid}/favorites/check-activity-exists`, activityUuid);
  }
}
