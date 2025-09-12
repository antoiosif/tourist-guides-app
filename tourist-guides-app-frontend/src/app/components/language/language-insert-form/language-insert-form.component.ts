import { Component, EventEmitter, inject, Output } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Errors } from 'src/app/shared/interfaces/error';
import { LanguageInsertDTO } from 'src/app/shared/interfaces/language';
import { LanguageService } from 'src/app/shared/services/language.service';

@Component({
  selector: 'app-language-insert-form',
  imports: [ReactiveFormsModule],
  templateUrl: './language-insert-form.component.html',
  styleUrl: './language-insert-form.component.css'
})
export class LanguageInsertFormComponent {
  // send the insertStatus to parent component to handle the alerts to be shown to the user
  @Output() onInsert = new EventEmitter<{success: boolean, message: string}>();

  // inform the parent that the button 'Cancel' is clicked
  @Output() onCancelInsert = new EventEmitter<void>();

  languageService: LanguageService = inject(LanguageService);
  validationErrors: Error[] = Errors;
  insertStatus: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };
  
  // Insert Language Form
  form: FormGroup = new FormGroup({
    code: new FormControl(null, [
      Validators.required,
      Validators.pattern('^[a-zA-Z]{3}$')
    ]),
    name: new FormControl(null, [
      Validators.required,
      Validators.minLength(2)
    ])
  });

  // Check if the input code already exists in DB
  checkCodeExists(): void {
    const inputCode = this.form.get('code')?.value;

    if (inputCode) {
      this.languageService.isCodeExists(inputCode)
        .subscribe((response) => {
          if (response) {
            this.form.get('code')?.setErrors({languageCodeExists: true});
          }
        })
    }
  }

  // Check if the input name already exists in DB
  checkNameExists(): void {
    const inputName = this.form.get('name')?.value.trim();

    if (inputName) {
      this.languageService.isNameExists(inputName)
        .subscribe((response) => {
          if (response) {
            this.form.get('name')?.setErrors({languageNameExists: true});
          }
        })
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const languageInsertDTO: LanguageInsertDTO = {
        code: this.form.get('code')?.value,
        name: this.form.get('name')?.value.trim()
      };

      this.languageService.insertLanguage(languageInsertDTO)
        .subscribe({
          next: (response) => {
            this.insertStatus = {success: true, message: 'Η εισαγωγή ολοκληρώθηκε με επιτυχία! Κάντε Refresh τη σελίδα για να εμφανιστούν οι αλλαγές.'};
            this.form.reset();
            this.onInsert.emit(this.insertStatus);
          },
          error: (response) => {
            this.insertStatus = {success: false, message: response.error.code};
            this.onInsert.emit(this.insertStatus);
          }
        })
    }
  }

  onCancel(): void {
    this.form.reset();
    this.onCancelInsert.emit();
  }

  // Getters for the form controls
  get code() {
    return this.form.get('code');
  } 
  get name() {
    return this.form.get('name');
  }
}
