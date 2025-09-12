import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { NgTemplateOutlet } from '@angular/common';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { Credentials, LoggedInUser } from 'src/app/shared/interfaces/user';
import { Errors } from 'src/app/shared/interfaces/error';
import { UserService } from 'src/app/shared/services/user.service';

@Component({
  selector: 'app-login-form',
  imports: [
    ReactiveFormsModule, 
    MatIconModule, 
    NgbTooltipModule, 
    NgTemplateOutlet
  ],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css'
})
export class LoginFormComponent {
  userService: UserService = inject(UserService);
  router: Router = inject(Router);

  validationErrors: Error[] = Errors;
  invalidLogin: boolean = false;

  // Login Form
  form: FormGroup = new FormGroup({
    username: new FormControl(null, [
      Validators.required, 
      Validators.email
    ]),
    password: new FormControl(null, [
      Validators.required,
      Validators.pattern("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&+=])^\\S{8,}$")
    ])
  });

  onSubmit(): void {
    if (this.form.valid) {
      const credentials: Credentials = this.form.value as Credentials;

      this.userService.loginUser(credentials)
        .subscribe({
          next: (response) => {
            const access_token: string = response.token;
            localStorage.setItem('access_token', access_token);

            const decodedTokenSubject = jwtDecode(access_token) as unknown as LoggedInUser;
            this.userService.user$.set({
              sub: decodedTokenSubject.sub,   // username
              uuid: decodedTokenSubject.uuid,
              firstname: decodedTokenSubject.firstname,
              lastname: decodedTokenSubject.lastname,
              role: decodedTokenSubject.role
            });

            // After the successful login user is navigated to Home page
            this.router.navigate(['']);
          },
          error: (response) => {
            this.invalidLogin = true; // set to 'True' for the alert to appear
          }
        })
    }
  }

  // Getters for the form controls
  get username() {
    return this.form.get('username');
  }
  get password() {
    return this.form.get('password');
  }
}
