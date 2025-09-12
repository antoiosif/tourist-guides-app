import { Component, inject, OnInit } from '@angular/core';
import { formatDate } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UserService } from 'src/app/shared/services/user.service';
import { GuideService } from 'src/app/shared/services/guide.service';
import { ActivityReadOnlyDTO } from 'src/app/shared/interfaces/activity';
import { AlertSuccessFailureComponent } from '../alert-success-failure/alert-success-failure.component';
import { GuideReadOnlyDTO } from 'src/app/shared/interfaces/guide';


@Component({
  selector: 'app-guide-favorites',
  imports: [
    RouterLink, 
    AlertSuccessFailureComponent
  ],
  templateUrl: './guide-favorites.component.html',
  styleUrl: './guide-favorites.component.css'
})
export class GuideFavoritesComponent implements OnInit {
  userService: UserService = inject(UserService);
  guideService: GuideService = inject(GuideService);
  activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);

  user = this.userService.user$;
  guideUuid: string | null = null;
  guide: GuideReadOnlyDTO | null = null;
  activities: ActivityReadOnlyDTO[] = [];
  status: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };

  ngOnInit() {
    // Generate Guide's UUID from the path
    this.guideUuid = this.activatedRoute.snapshot.paramMap.get("uuid");

    if (this.guideUuid !== this.user()?.uuid) {
      this.router.navigate(['/not-authorized']);
    }

    // Get Guide
    if (this.guideUuid) {
      this.guideService.getGuideByUuid(this.guideUuid)
        .subscribe({
          next: (response) => {
            this.guide = response;

            // Get the activities
            this.guideService.getGuideActivities(this.user()?.uuid)
              .subscribe({
                next: (response) => {
                  this.activities = response;
                },
                error: (response) => {
                  this.status = {success: false, message: response.error.code};
                }
              })
          },
          error: (response) => {
            this.status = {success: false, message: response.error.code};
          }
        })
    }
  }

  formatDate(dateTime: string): string {
    return formatDate(dateTime, 'dd-MM-yyyy', 'en');
  }
}
