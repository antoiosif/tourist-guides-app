import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { formatDate, ViewportScroller, Location } from '@angular/common';
import { ActivityService } from 'src/app/shared/services/activity.service';
import { CategoryService } from 'src/app/shared/services/category.service';
import { BackendErrors, Errors } from 'src/app/shared/interfaces/error';
import { ActivityReadOnlyDTO, ActivityUpdateDTO } from 'src/app/shared/interfaces/activity';
import { LinkGoBackToComponent } from '../link-go-back-to/link-go-back-to.component';
import { AlertSuccessFailureComponent } from '../alert-success-failure/alert-success-failure.component';
import { AlertDeleteConfirmationComponent } from "../alert-delete-confirmation/alert-delete-confirmation.component";

@Component({
  selector: 'app-activity-edit',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    LinkGoBackToComponent,
    AlertSuccessFailureComponent,
    AlertDeleteConfirmationComponent
  ],
  templateUrl: './activity-edit.component.html',
  styleUrl: './activity-edit.component.css'
})
export class ActivityEditComponent implements OnInit {
  activityService: ActivityService = inject(ActivityService);
  categoryService: CategoryService = inject(CategoryService);
  activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  viewportScroller: ViewportScroller = inject(ViewportScroller);
  location: Location = inject(Location);
  
  categories = this.categoryService.categoriesSorted$;
  validationErrors: Error[] = Errors;
  backendErrors: Error[] = BackendErrors;
  activityUuid: string | null = null;
  activity: ActivityReadOnlyDTO | null = null;
  categoryId: bigint | null = null;
  isConfirmationAlertVisible: boolean = false;  // for Delete
  status: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };

  // Update Activity Form
  form: FormGroup = new FormGroup({
    id: new FormControl({value: null, disabled: true}),
    createdAt: new FormControl({value: null, disabled: true}),
    updatedAt: new FormControl({value: null, disabled: true}),
    uuid: new FormControl({value: null, disabled: true}),
    title: new FormControl(null, [
      Validators.required,
      Validators.maxLength(90)
    ]),
    description: new FormControl(null, [
      Validators.required,
      Validators.maxLength(1000)
    ]),
    dateTime: new FormControl(null, Validators.required),
    price: new FormControl(null, Validators.required),
    activityStatus: new FormControl('0', Validators.required),
    categoryId: new FormControl('', Validators.required)
  });

  ngOnInit(): void {
    // Generate Activity's UUID from the path
    this.activityUuid = this.activatedRoute.snapshot.paramMap.get("uuid");
    
    // Get Activity
    if (this.activityUuid) {
      this.activityService.getActivityByUuid(this.activityUuid)
        .subscribe({
          next: (response) => {
            this.activity = response;

            // Populate the form with the current state of the Activity
            // Get category id
            this.categoryService.getCategoryByName(this.activity.category)
              .subscribe({
                next: (response) => {
                  this.categoryId = response.id;
                  this.setValues();      
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
    if (this.activity) {
      this.form.setValue({
        id: this.activity?.id,
        createdAt: formatDate(this.activity?.createdAt, 'dd/MM/yyyy HH:mm:ss', 'en'),
        updatedAt: formatDate(this.activity?.updatedAt, 'dd/MM/yyyy HH:mm:ss', 'en'),
        uuid: this.activity?.uuid,
        title: this.activity?.title,
        description: this.activity?.description,
        dateTime: formatDate(this.activity?.dateTime, 'yyyy-MM-dd HH:mm:ss', 'en'),
        price: this.activity?.price,
        activityStatus: this.activity?.status,
        categoryId: this.categoryId
      });
    }
  }

  // for Update
  onSubmit(): void {
    if (this.form.valid) {
      const activityUpdateDTO: ActivityUpdateDTO = {
        id: this.form.get('id')?.value,
        uuid: this.form.get('uuid')?.value,
        title: this.form.get('title')?.value,
        description: this.form.get('description')?.value,
        dateTime: formatDate(this.form.get('dateTime')?.value, 'yyyy-MM-ddTHH:mm:ss', 'en') ,
        price: this.form.get('price')?.value,
        status: this.form.get('activityStatus')?.value,
        categoryId: this.form.get('categoryId')?.value
      };
      
      if (this.activityUuid) {
        this.activityService.updateActivity(this.activityUuid, activityUpdateDTO)
        .subscribe({
          next: (response) => {
            this.status = {success: true, message: 'Η ενημέρωση ολοκληρώθηκε με επιτυχία!'};
          },
          error: (response) => {
            this.status = {success: false, message: response.error.code};
          }
        })
      }
      this.scrollToTop(); // scroll to the top of page where the alert is shown 
    }
  }

  onCancel() {
    this.goBack();
  }

  goBack(): void {
    this.location.back();
  }

  // for Delete
  setToDelete(uuid: string | null): void {
    this.isConfirmationAlertVisible = true; // set status to 'True' for the alert to appear
    this.scrollToTop(); // scroll to the top of page where the alert is shown
  }

  proceedToDelete(uuid: string | null): void {
    if (uuid) {
      this.activityService.deleteActivity(uuid)
        .subscribe({
            next: (response) => {
              this.status = {success: true, message: 'Η διαγραφή ολοκληρώθηκε με επιτυχία!'};
              this.form.disable(); 
            },
            error: (response) => {
              this.status = {success: false, message: response.error.code};
            }
          })
      this.isConfirmationAlertVisible = false;  // set status to 'False' for the confirmation alert to disappear
    }
  }

  cancelDelete(): void {
    this.isConfirmationAlertVisible = false;
  }

  scrollToTop() {
    this.viewportScroller.scrollToPosition([0, 0]);
  }

  // Getters for the form controls
  get title() {
    return this.form.get('title');
  }
  get description() {
    return this.form.get('description');
  }
  get dateTime() {
    return this.form.get('dateTime');
  }
  get price() {
    return this.form.get('price');
  }
  get activityStatus() {
    return this.form.get('activityStatus');
  }
}
