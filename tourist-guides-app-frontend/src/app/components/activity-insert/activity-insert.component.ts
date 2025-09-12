import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ViewportScroller, Location } from '@angular/common';
import { BackendErrors, Errors } from 'src/app/shared/interfaces/error';
import { ActivityService } from 'src/app/shared/services/activity.service';
import { CategoryService } from 'src/app/shared/services/category.service';
import { ActivityInsertDTO } from 'src/app/shared/interfaces/activity';
import { LinkGoBackToComponent } from '../link-go-back-to/link-go-back-to.component';
import { AlertSuccessFailureComponent } from '../alert-success-failure/alert-success-failure.component';

@Component({
  selector: 'app-activity-insert',
  imports: [
    ReactiveFormsModule,
    RouterLink,
    LinkGoBackToComponent,
    AlertSuccessFailureComponent
  ],
  templateUrl: './activity-insert.component.html',
  styleUrl: './activity-insert.component.css'
})
export class ActivityInsertComponent {
  activityService: ActivityService = inject(ActivityService);
  categoryService: CategoryService = inject(CategoryService);
  viewportScroller: ViewportScroller = inject(ViewportScroller);
  location: Location = inject(Location);
  
  categories = this.categoryService.categoriesSorted$;
  validationErrors: Error[] = Errors;
  backendErrors: Error[] = BackendErrors;
  insertStatus: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };

  // Insert Activity Form
  form: FormGroup = new FormGroup({
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
    status: new FormControl('ACTIVE', Validators.required),
    categoryId: new FormControl('', Validators.required)
  });

  onSubmit(): void {
    if (this.form.valid) {
      const activityInsertDTO: ActivityInsertDTO = {
        title: this.form.get('title')?.value.trim(),
        description: this.form.get('description')?.value.trim(),
        dateTime: this.form.get('dateTime')?.value,
        price: this.form.get('price')?.value,
        status: this.form.get('status')?.value,
        categoryId: this.form.get('categoryId')?.value
      };

      this.activityService.insertActivity(activityInsertDTO)
        .subscribe({
          next: (response) => {
            this.insertStatus = {success: true, message: 'Η εισαγωγή ολοκληρώθηκε με επιτυχία!'};
            this.resetForm();
          },
          error: (response) => {
            this.insertStatus = {success: false, message: response.error.code};
          }
        })
      this.scrollToTop(); // scroll to the top of page where the alert is shown 
    }
  }

  onCancel(): void {
    this.goBack();
  }

  resetForm(): void {
    this.form.reset();

    // Set initial values
    this.form.get('status')?.setValue('0');
    this.form.get('categoryId')?.setValue('');
  }

  goBack(): void {
    this.location.back();
  }

  scrollToTop(): void {
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
  get status() {
    return this.form.get('status');
  }
  get categoryId() {
    return this.form.get('categoryId');
  }
}
