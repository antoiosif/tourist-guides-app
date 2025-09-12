import { Component, inject, OnInit } from '@angular/core';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { GuideReadOnlyDTO } from 'src/app/shared/interfaces/guide';
import { GuideService } from 'src/app/shared/services/guide.service';
import { BackendErrors } from 'src/app/shared/interfaces/error';
import { LinkGoBackToComponent } from "../link-go-back-to/link-go-back-to.component";

@Component({
  selector: 'app-guide-profile',
  imports: [
    NgbAlertModule, 
    LinkGoBackToComponent
  ],
  templateUrl: './guide-profile.component.html',
  styleUrl: './guide-profile.component.css'
})
export class GuideProfileComponent implements OnInit {
  guideService: GuideService = inject(GuideService);
  activatedRoute: ActivatedRoute = inject(ActivatedRoute);

  backendErrors: Error[] = BackendErrors;
  guideUuid: string | null = null;             // uuid of the guide to get
  guide: GuideReadOnlyDTO | undefined;    // the guide to get
  status: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };
  
  ngOnInit(): void {
    // Generate Guide's UUID from the path
    this.guideUuid = this.activatedRoute.snapshot.paramMap.get("uuid");

    // Get Guide
    if (this.guideUuid) {
      this.guideService.getGuideByUuid(this.guideUuid)
        .subscribe({
          next: (response) => {
            this.guide = response;

            // Check if Guide is active
            if (this.guide.isActive) {
              this.status = {success: true, message: 'Guide was found'};
            } else {
              this.status = {success: false, message: 'GuideInvalidArgument'};
            }
          },
          error: (response) => {
            this.status = {success: false, message: response.error.code};
          }
      })
    }
  }
}
