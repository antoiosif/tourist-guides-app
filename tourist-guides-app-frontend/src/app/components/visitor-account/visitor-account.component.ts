import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { formatDate, ViewportScroller, Location } from "@angular/common";
import { UserService } from 'src/app/shared/services/user.service';
import { VisitorService } from 'src/app/shared/services/visitor.service';
import { RegionService } from 'src/app/shared/services/region.service';
import { BackendErrors, Errors } from 'src/app/shared/interfaces/error';
import { VisitorReadOnlyDTO, VisitorUpdateDTO } from 'src/app/shared/interfaces/visitor';
import { LinkGoBackToComponent } from "../link-go-back-to/link-go-back-to.component";
import { AlertSuccessFailureComponent } from "../alert-success-failure/alert-success-failure.component";
import { AlertDeleteConfirmationComponent } from "../alert-delete-confirmation/alert-delete-confirmation.component";

@Component({
  selector: 'app-visitor-account',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    LinkGoBackToComponent,
    AlertSuccessFailureComponent,
    AlertDeleteConfirmationComponent
  ],
  templateUrl: './visitor-account.component.html',
  styleUrl: './visitor-account.component.css'
})
export class VisitorAccountComponent implements OnInit {
  userService: UserService = inject(UserService);
  visitorService: VisitorService = inject(VisitorService);
  regionService: RegionService = inject(RegionService);
  activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  viewportScroller: ViewportScroller = inject(ViewportScroller);
  router: Router = inject(Router);
  location: Location = inject(Location);

  user = this.userService.user$;
  regions = this.regionService.regionsSorted$;
  validationErrors: Error[] = Errors;
  backendErrors: Error[] = BackendErrors;
  visitorUuid: string | null = null;
  visitor: VisitorReadOnlyDTO | null = null;
  regionId: bigint | null = null;
  notActive: boolean = false;
  isConfirmationAlertVisible: boolean = false;  // for Delete
  status: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };

  // Update Visitor Form
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
    gender: new FormControl(null),
    regionId: new FormControl(''),
  });

  ngOnInit(): void {
    // Generate Visitor's UUID from the path
    this.visitorUuid = this.activatedRoute.snapshot.paramMap.get("uuid");

    // If the Logged In user is not 'SUPER_ADMIN' and the account is not the current Visitor's account then the user is Not Authorized
    if (this.user()?.role !== 'SUPER_ADMIN' && this.visitorUuid !== this.user()?.uuid) {
      this.router.navigate(['/not-authorized']);
    }

    // Get Visitor
    if (this.visitorUuid) {
      this.visitorService.getVisitorByUuid(this.visitorUuid)
        .subscribe({
          next: (response) => {
            this.visitor = response;

            // Populate the form with the current state of the Visitor
            // Get region id (it can be null)
            if(this.visitor.region) {
              this.regionService.getRegionByName(this.visitor.region)
                .subscribe({
                  next: (response) => {
                    this.regionId = response.id;
                    this.setValues();
                  },
                  error: (response) => {
                    this.status = {success: false, message: response.error.code};
                  }
                })
            } else {
              this.setValues();
            }
            
            // If the Visitor is not active, the account (and the form) is disabled
            if (!this.visitor.isActive) {
              this.status = {success: false, message: 'VisitorInvalidArgument'};
              this.form.disable();
            }
            
          },
          error: (response) => {
            this.status = {success: false, message: response.error.code};
            this.form.disable();
          }
        })
    }
  }

  setValues(): void {
    if (this.visitor) {
      this.form.setValue({
        userIsActive: this.visitor.userReadOnlyDTO.isActive,
        userId: this.visitor.userReadOnlyDTO.id,
        username: this.visitor.userReadOnlyDTO.username,
        password: this.visitor.userReadOnlyDTO.password,
        isActive: this.visitor.isActive,
        id: this.visitor.id,
        createdAt: formatDate(this.visitor.createdAt, 'dd/MM/yyyy HH:mm:ss', 'en'),
        updatedAt: formatDate(this.visitor.updatedAt, 'dd/MM/yyyy HH:mm:ss', 'en'),
        uuid: this.visitor.uuid,
        firstname: this.visitor.userReadOnlyDTO.firstname,
        lastname: this.visitor.userReadOnlyDTO.lastname,
        gender: this.visitor.userReadOnlyDTO.gender,
        regionId: this.regionId || '',
      });
    }
  }

  // for Update
  onSubmit(): void {
    if (this.form.valid) {
      const visitorUpdateDTO: VisitorUpdateDTO = {
        id: this.form.get('id')?.value,
        isActive: this.form.get('isActive')?.value,
        uuid: this.form.get('uuid')?.value,
        userUpdateDTO: {
          id: this.form.get('userId')?.value,
          isActive: this.form.get('userIsActive')?.value,
          username: this.form.get('username')?.value,
          password: this.form.get('password')?.value,
          firstname: this.form.get('firstname')?.value,
          lastname: this.form.get('lastname')?.value,
          gender: this.form.get('gender')?.value,
          role: 'VISITOR',
        },
        regionId: this.form.get('regionId')?.value,
      };

      if(this.visitorUuid) {
        this.visitorService.updateVisitor(this.visitorUuid, visitorUpdateDTO)
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
      this.visitorService.deleteVisitor(uuid)
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
}
