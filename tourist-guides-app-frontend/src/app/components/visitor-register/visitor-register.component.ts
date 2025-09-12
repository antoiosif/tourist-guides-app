import { Component, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule, FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { NgTemplateOutlet, ViewportScroller } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BackendErrors, Errors } from 'src/app/shared/interfaces/error';
import { VisitorService } from 'src/app/shared/services/visitor.service';
import { RegionService } from 'src/app/shared/services/region.service';
import { VisitorInsertDTO } from 'src/app/shared/interfaces/visitor';
import { AlertSuccessFailureComponent } from '../alert-success-failure/alert-success-failure.component';

@Component({
  selector: 'app-visitor-register',
  imports: [
    MatIconModule,
    ReactiveFormsModule,
    NgbTooltipModule,
    NgTemplateOutlet,
    RouterLink,
    AlertSuccessFailureComponent
  ],
  templateUrl: './visitor-register.component.html',
  styleUrl: './visitor-register.component.css'
})
export class VisitorRegisterComponent {
  visitorService: VisitorService = inject(VisitorService);
  regionService: RegionService = inject(RegionService);
  viewportScroller: ViewportScroller = inject(ViewportScroller);

  regions = this.regionService.regionsSorted$;
  validationErrors: Error[] = Errors;
  backendErrors: Error[] = BackendErrors;
  status: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };

  // Register Visitor Form
  form: FormGroup = new FormGroup({
    username: new FormControl(null, [
      Validators.required, 
      Validators.email
    ]),
    password: new FormControl(null, [
      Validators.required,
      Validators.pattern("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&+=])^\\S{8,}$")
    ]),
    confirmPassword: new FormControl(null, Validators.required),
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
  }, this.passwordConfirmValidator);

  // Check if password and confirmPassword are the same
  passwordConfirmValidator(control: AbstractControl): {[key: string]: boolean} | null {
    const form = control as FormGroup;
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    if (password && confirmPassword && (password !== confirmPassword)) {
      form.get('confirmPassword')?.setErrors({passwordMismatch: true});
      return {passwordMismatch: true};
    }
    return null;
  }

  // Check if the input username already exists in DB
  checkUsernameExists(): void {
    const inputUsername = this.form.get('username')?.value;

    if (inputUsername) {
      this.visitorService.isUsernameExists(inputUsername)
        .subscribe((response) => {
          if (response) {
            this.form.get('username')?.setErrors({usernameExists: true});
          }
        })
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const visitorInsertDTO: VisitorInsertDTO = {
        userInsertDTO: {
          username: this.form.get('username')?.value,
          password: this.form.get('password')?.value,
          firstname: this.form.get('firstname')?.value,
          lastname: this.form.get('lastname')?.value,
          gender: this.form.get('gender')?.value
        },
        regionId: this.form.get('regionId')?.value
      };

      this.visitorService.insertVisitor(visitorInsertDTO)
        .subscribe({
          next: (response) => {
            this.status = {success: true, message: 'Η εγγραφή ολοκληρώθηκε με επιτυχία!'};
            this.form.reset();
          },
          error: (response) => {
            this.status = {success: false, message: response.error.code};
          }
        })
      this.scrollToTop(); // scroll to the top of page where the alert is shown
    }
  }

  scrollToTop() {
    this.viewportScroller.scrollToPosition([0, 0]);
  }

  // Getters for the form controls
  get username() {
    return this.form.get('username');
  }
  get password() {
    return this.form.get('password');
  }
  get confirmPassword() {
    return this.form.get('confirmPassword');
  }
  get firstname() {
    return this.form.get('firstname');
  }
  get lastname() {
    return this.form.get('lastname');
  }
}
