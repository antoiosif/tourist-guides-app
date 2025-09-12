import { Component, inject, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { formatDate } from '@angular/common';
import { UserService } from 'src/app/shared/services/user.service';
import { ActivityService } from 'src/app/shared/services/activity.service';
import { BackendErrors } from 'src/app/shared/interfaces/error';
import { ActivityReadOnlyDTO } from 'src/app/shared/interfaces/activity';
import { LinkGoBackComponent } from "../link-go-back/link-go-back.component";
import { GuideService } from 'src/app/shared/services/guide.service';

@Component({
  selector: 'app-activity-profile',
  imports: [
    MatIconModule,
    NgbAlertModule,
    LinkGoBackComponent
],
  templateUrl: './activity-profile.component.html',
  styleUrl: './activity-profile.component.css'
})
export class ActivityProfileComponent implements OnInit {
  userService: UserService = inject(UserService);
  activityService: ActivityService = inject(ActivityService);
  guideService: GuideService = inject(GuideService);
  activatedRoute: ActivatedRoute = inject(ActivatedRoute);

  user = this.userService.user$;
  backendErrors: Error[] = BackendErrors;
  activityUuid: string | null = null;           // uuid of the activity to get
  activity: ActivityReadOnlyDTO | undefined;    // the activity to get
  isInFavorites: boolean | null = null;
  status: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };

  favorite: string = '';
  
  ngOnInit(): void {
    // Generate Activity's UUID from the path
    this.activityUuid = this.activatedRoute.snapshot.paramMap.get("uuid");

    // Get Activity
    if (this.activityUuid) {
      this.activityService.getActivityByUuid(this.activityUuid)
        .subscribe({
          next: (response) => {
            this.activity = response;
          },
          error: (response) => {
            this.status = {success: false, message: response.error.code};
          }
      })
    }

    if (this.activityUuid) {
      this.guideService.isActivityExistsInGuide(this.user()?.uuid, this.activityUuid)
        .subscribe((response) => {
            this.isInFavorites = response;
        });
    }
  }

  onAddToFavorites(): void {
    if (this.activityUuid) {
      this.guideService.addActivityToGuide(this.user()?.uuid, this.activityUuid)
        .subscribe({
          next: (response) => {
            this.status = {success: true, message: 'Η δραστηριότητα προστέθηκε στα Αγαπημένα'};
            this.isInFavorites = true;
          },
          error: (response) => {
            this.status = {success: false, message: response.error.code};
          }
        })
      }
  }

  onRemoveFromFavorites(): void {
    if (this.activityUuid) {
      this.guideService.removeActivityFromGuide(this.user()?.uuid, this.activityUuid)
        .subscribe({
          next: (response) => {
            this.status = {success: true, message: 'Η δραστηριότητα αφαιρέθηκε από τα Αγαπημένα'};
            this.isInFavorites = false;
          },
          error: (response) => {
            this.status = {success: false, message: response.error.code};
          }
        })
      }
  }

  formatDate(dateTime: string | undefined): string {
    if(dateTime) {
      return formatDate(dateTime, 'dd-MM-yyyy', 'en');
    }
    return '';
  }
}
