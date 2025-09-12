import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { formatDate, ViewportScroller, Location } from "@angular/common";
import { UserService } from 'src/app/shared/services/user.service';
import { GuideService } from 'src/app/shared/services/guide.service';
import { RegionService } from 'src/app/shared/services/region.service';
import { LanguageService } from 'src/app/shared/services/language.service';
import { BackendErrors, Errors } from 'src/app/shared/interfaces/error';
import { GuideReadOnlyDTO, GuideUpdateDTO } from 'src/app/shared/interfaces/guide';
import { LinkGoBackToComponent } from "../link-go-back-to/link-go-back-to.component";
import { AlertSuccessFailureComponent } from "../alert-success-failure/alert-success-failure.component";
import { AlertDeleteConfirmationComponent } from "../alert-delete-confirmation/alert-delete-confirmation.component";

@Component({
  selector: 'app-guide-account',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    LinkGoBackToComponent,
    AlertSuccessFailureComponent,
    AlertDeleteConfirmationComponent
],
  templateUrl: './guide-account.component.html',
  styleUrl: './guide-account.component.css'
})
export class GuideAccountComponent implements OnInit {
  userService: UserService = inject(UserService);
  guideService: GuideService = inject(GuideService);
  regionService: RegionService = inject(RegionService);
  languageService: LanguageService = inject(LanguageService);
  activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  viewportScroller: ViewportScroller = inject(ViewportScroller);
  router: Router = inject(Router);
  location: Location = inject(Location);
  
  user = this.userService.user$;
  regions = this.regionService.regionsSorted$;
  languages = this.languageService.languagesSorted$;
  validationErrors: Error[] = Errors;
  backendErrors: Error[] = BackendErrors;
  guideUuid: string | null = null;
  guide: GuideReadOnlyDTO | null = null;
  regionId: bigint | null = null;
  languageId: bigint | null = null;
  notActive: boolean = false;
  isConfirmationAlertVisible: boolean = false;  // for Delete
  status: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };
  
  // Update Guide Form
  form: FormGroup = new FormGroup({
    userIsActive: new FormControl({value: null, disabled: true}),
    userId: new FormControl({value: null, disabled: true}),
    username: new FormControl({value: null, disabled: true}),
    password: new FormControl({value: null, disabled: true}),
    isActive: new FormControl({value: null, disabled: true}),
    id: new FormControl({value: null, disabled: true}),
    createdAt: new FormControl({value: null, disabled: true}),
    updatedAt: new FormControl({value: null, disabled: true}),
    uuid: new FormControl({value: null, disabled: true}),
    firstname: new FormControl(null, [
      Validators.required,
      Validators.pattern("^\\S{2,}$")
    ]),
    lastname: new FormControl(null, [
      Validators.required,
      Validators.pattern("^\\S{2,}$")
    ]),
    recordNumber: new FormControl(null, [
      Validators.required,
      Validators.pattern("^\\S{5,}$")
    ]),
    dateOfIssue: new FormControl(null, Validators.required),
    gender: new FormControl(null),
    phoneNumber: new FormControl(null, Validators.pattern("^\\d{10}$")),
    email: new FormControl(null, Validators.email),
    regionId: new FormControl(null, Validators.required),
    languageId: new FormControl(null, Validators.required),
    bio: new FormControl(null, Validators.maxLength(5000))
  });

  ngOnInit(): void {
    // Generate Guide's UUID from the path
    this.guideUuid = this.activatedRoute.snapshot.paramMap.get("uuid");

    // If the Logged In user is not 'SUPER_ADMIN' and the account is not the current Guide's account then the user is Not Authorized
    if (this.user()?.role !== 'SUPER_ADMIN' && this.guideUuid !== this.user()?.uuid) {
      this.router.navigate(['/not-authorized']);
    }
    
    // Get Guide
    if (this.guideUuid) {
      this.guideService.getGuideByUuid(this.guideUuid)
        .subscribe({
          next: (response) => {
            this.guide = response;

            // Populate the form with the current state of the Guide
            // Get region id
            this.regionService.getRegionByName(this.guide.region)
              .subscribe({
                next: (response) => {
                  this.regionId = response.id;
                  
                  // Get language id
                  if (this.guide) {
                    this.languageService.getLanguageByName(this.guide.language)
                      .subscribe({
                        next: (response) => {
                          this.languageId = response.id;
                          this.setValues();

                          // If the Guide is not active, the account (and the form) is disabled
                          if (!this.guide?.isActive) {
                            this.status = {success: false, message: 'GuideInvalidArgument'};
                            this.form.disable();
                          }
                        },
                        error: (response) => {
                          this.status = {success: false, message: response.error.code};
                        }
                      })
                  }
                },
                error: (response) => {
                  this.status = {success: false, message: response.error.code};
                }
              })
          },
          error: (response) => {
            this.status = {success: false, message: response.error.code};
            this.form.disable();
          }
        })
    }
  }

  setValues(): void {
    if (this.guide) {
      this.form.setValue({
        userIsActive: this.guide?.userReadOnlyDTO.isActive,
        userId: this.guide?.userReadOnlyDTO.id,
        username: this.guide?.userReadOnlyDTO.username,
        password: this.guide?.userReadOnlyDTO.password,
        isActive: this.guide?.isActive,
        id: this.guide?.id,
        createdAt: formatDate(this.guide?.createdAt, 'dd/MM/yyyy HH:mm:ss', 'en'),
        updatedAt: formatDate(this.guide?.updatedAt, 'dd/MM/yyyy HH:mm:ss', 'en'),
        uuid: this.guide?.uuid,
        firstname: this.guide?.userReadOnlyDTO.firstname,
        lastname: this.guide?.userReadOnlyDTO.lastname,
        recordNumber: this.guide?.recordNumber,
        dateOfIssue: formatDate(this.guide?.dateOfIssue, 'yyyy-MM-dd', 'en'),
        gender: this.guide?.userReadOnlyDTO.gender,
        phoneNumber: this.guide?.phoneNumber,
        email: this.guide?.email,
        regionId: this.regionId,
        languageId: this.languageId,
        bio: this.guide?.bio
      });
    }
  }

  // Check if the input record number already exists in DB and that the input value is not the same with the value of the Guide to update
  checkRecordNumberExists(): void {
    const inputRecordNumber = this.form.get('recordNumber')?.value;

    if (inputRecordNumber && inputRecordNumber !== this.guide?.recordNumber) {
      this.guideService.isRecordNumberExists(inputRecordNumber)
        .subscribe((response) => {
          if (response) {
            this.form.get('recordNumber')?.setErrors({recordNumberExists: true});
          } 
        })
    }
  }

  // for Update
  onSubmit(): void {
    if (this.form.valid) {
      const guideUpdateDTO: GuideUpdateDTO = {
        id: this.form.get('id')?.value,
        isActive: this.form.get('isActive')?.value,
        uuid: this.form.get('uuid')?.value,
        recordNumber: this.form.get('recordNumber')?.value,
        dateOfIssue: this.form.get('dateOfIssue')?.value,
        phoneNumber: this.form.get('phoneNumber')?.value,
        email: this.form.get('email')?.value,
        bio: this.form.get('bio')?.value,
        userUpdateDTO: {
          id: this.form.get('userId')?.value,
          isActive: this.form.get('userIsActive')?.value,
          username: this.form.get('username')?.value,
          password: this.form.get('password')?.value,
          firstname: this.form.get('firstname')?.value,
          lastname: this.form.get('lastname')?.value,
          gender: this.form.get('gender')?.value,
          role: 'GUIDE',
        },
        regionId: this.form.get('regionId')?.value,
        languageId: this.form.get('languageId')?.value
      };

      if(this.guideUuid) {
        this.guideService.updateGuide(this.guideUuid, guideUpdateDTO)
          .subscribe({
            next: (response) => {
              this.status = {success: true, message: 'Η ενημέρωση ολοκληρώθηκε με επιτυχία!'};
            },
            error: (response) => {
              this.status = {success: false, message: response.error.code};
            }
          })
        this.scrollToTop(); // scroll to the top of page where the alert is shown
      }
    }
  }

  onCancel(): void {
    this.goBack();
  }

  goBack(): void {
    this.location.back();
  }

  // for Delete
  setToDelete(uuid: string | null): void {
    this.isConfirmationAlertVisible = true; // set status to 'true' for the alert to appear
    this.scrollToTop(); // scroll to the top of page where the alert is shown
  }

  proceedToDelete(uuid: string | null): void {
    if (uuid) {
      this.guideService.deleteGuide(uuid)
        .subscribe({
            next: (response) => {
              if (this.user()?.role !== 'SUPER_ADMIN') this.userService.logoutUser();
              this.status = {success: true, message: 'Η απενεργοποίηση ολοκληρώθηκε με επιτυχία!'};
              this.form.disable(); 
            },
            error: (response) => {
              this.status = {success: false, message: response.error.code};
            }
          })
      this.isConfirmationAlertVisible = false;  // set status to 'false' for the confirmation alert to disappear
    }
  }

  cancelDelete(): void {
    this.isConfirmationAlertVisible = false;
  }

  scrollToTop() {
    this.viewportScroller.scrollToPosition([0, 0]);
  }

  // Getters for the form controls
  get firstname() {
    return this.form.get('firstname');
  }
  get lastname() {
    return this.form.get('lastname');
  }
  get recordNumber() {
    return this.form.get('recordNumber');
  }
  get dateOfIssue() {
    return this.form.get('dateOfIssue');
  }
  get phoneNumber() {
    return this.form.get('phoneNumber');
  }
  get email() {
    return this.form.get('email');
  }
  get bio() {
    return this.form.get('bio');
  }
}
