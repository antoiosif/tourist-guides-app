import { Component, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule, FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { NgTemplateOutlet, ViewportScroller } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BackendErrors, Errors } from 'src/app/shared/interfaces/error';
import { GuideService } from 'src/app/shared/services/guide.service';
import { RegionService } from 'src/app/shared/services/region.service';
import { LanguageService } from 'src/app/shared/services/language.service';
import { GuideInsertDTO } from 'src/app/shared/interfaces/guide';
import { AlertSuccessFailureComponent } from '../alert-success-failure/alert-success-failure.component';

@Component({
  selector: 'app-guide-register',
  imports: [
    MatIconModule,
    ReactiveFormsModule,
    NgbTooltipModule,
    NgTemplateOutlet,
    RouterLink,
    AlertSuccessFailureComponent
  ],
  templateUrl: './guide-register.component.html',
  styleUrl: './guide-register.component.css'
})
export class GuideRegisterComponent {
  guideService: GuideService = inject(GuideService);
  regionService: RegionService = inject(RegionService);
  languageService: LanguageService = inject(LanguageService);
  viewportScroller: ViewportScroller = inject(ViewportScroller);
  
  regions = this.regionService.regionsSorted$;
  languages = this.languageService.languagesSorted$;
  validationErrors: Error[] = Errors;
  backendErrors: Error[] = BackendErrors;
  status: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };

  // Register Guide Form
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
    recordNumber: new FormControl(null, [
      Validators.required,
      Validators.pattern("^\\S{5,}$")
    ]),
    dateOfIssue: new FormControl(null, Validators.required),
    phoneNumber: new FormControl(null, Validators.pattern("^\\d{10}$")),
    email: new FormControl(null, Validators.email),
    regionId: new FormControl('', Validators.required),
    languageId: new FormControl('', Validators.required),
    bio: new FormControl(null, Validators.maxLength(5000))
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
      this.guideService.isUsernameExists(inputUsername)
        .subscribe((response) => {
          if (response) {
            this.form.get('username')?.setErrors({usernameExists: true});
          }
        })
    }
  }

  // Check if the input record number already exists in DB
  checkRecordNumberExists(): void {
    const inputRecordNumber = this.form.get('recordNumber')?.value.trim();

    if (inputRecordNumber) {
      this.guideService.isRecordNumberExists(inputRecordNumber)
        .subscribe((response) => {
          if (response) {
            this.form.get('recordNumber')?.setErrors({recordNumberExists: true});
          }
        })
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const guideInsertDTO: GuideInsertDTO = {
        recordNumber: this.form.get('recordNumber')?.value,
        dateOfIssue: this.form.get('dateOfIssue')?.value,
        phoneNumber: this.form.get('phoneNumber')?.value,
        email: this.form.get('email')?.value,
        bio: this.form.get('bio')?.value,
        userInsertDTO: {
          username: this.form.get('username')?.value,
          password: this.form.get('password')?.value,
          firstname: this.form.get('firstname')?.value,
          lastname: this.form.get('lastname')?.value,
          gender: this.form.get('gender')?.value
        },
        regionId: this.form.get('regionId')?.value,
        languageId: this.form.get('languageId')?.value
      };

      this.guideService.insertGuide(guideInsertDTO)
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
  get regionId() {
    return this.form.get('regionId');
  }
  get languageId() {
    return this.form.get('languageId');
  }
  get bio() {
    return this.form.get('bio');
  }
}
