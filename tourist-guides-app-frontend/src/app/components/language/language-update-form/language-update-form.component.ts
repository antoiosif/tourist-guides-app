import { Component, EventEmitter, inject, Input, OnChanges, Output } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Errors } from 'src/app/shared/interfaces/error';
import { LanguageReadOnlyDTO, LanguageUpdateDTO } from 'src/app/shared/interfaces/language';
import { LanguageService } from 'src/app/shared/services/language.service';

@Component({
  selector: 'app-language-update-form',
  imports: [ReactiveFormsModule],
  templateUrl: './language-update-form.component.html',
  styleUrl: './language-update-form.component.css'
})
export class LanguageUpdateFormComponent implements OnChanges {
  // send the updateStatus to parent component to handle the alerts to be shown to the user
  @Output() onUpdate = new EventEmitter<{success: boolean, message: string}>();

  // inform the parent that the button 'Cancel' is clicked
  @Output() onCancelUpdate = new EventEmitter<void>();

  @Input() idToUpdate: bigint | undefined;
  languageService: LanguageService = inject(LanguageService);
  validationErrors: Error[] = Errors;
  languageToUpdate: LanguageReadOnlyDTO | null = null;
  updateStatus: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };
  
  // Update Language Form
  form: FormGroup = new FormGroup({
    id: new FormControl({value: null, disabled: true}),
    code: new FormControl(null, [
      Validators.required,
      Validators.pattern('^[a-zA-Z]{3}$')
    ]),
    name: new FormControl(null, [
      Validators.required,
      Validators.minLength(2)
    ])
  });

  ngOnChanges(): void {
    // Get Language
    if (this.idToUpdate) {
      this.languageService.getLanguageById(this.idToUpdate)
        .subscribe({
          next: (response) => {
            this.languageToUpdate = response;
            this.setValues();
          },
          error: (response) => {
            this.updateStatus = {success: false, message: response.error.code};
          }
        })
    }
  }

  setValues(): void {
    this.form.setValue({
      id: this.languageToUpdate?.id,
      code: this.languageToUpdate?.code,
      name: this.languageToUpdate?.name,
    });
  }

  // Check if the input code already exists in DB and that the input value is not the same with the value of the Language to update
  checkCodeExists(): void {
    const inputCode = this.form.get('code')?.value;

    if (inputCode && inputCode !== this.languageToUpdate?.code) {
      this.languageService.isCodeExists(inputCode)
        .subscribe((response) => {
          if (response) {
            this.form.get('code')?.setErrors({languageCodeExists: true});
          }
        })
    }
  }

  // Check if the input name already exists in DB and that the input value is not the same with the value of the Language to update
  checkNameExists(): void {
    const inputName = this.form.get('name')?.value.trim();

    if (inputName && inputName !== this.languageToUpdate?.name) {
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
      const languageUpdateDTO: LanguageUpdateDTO = {
        id: this.form.get('id')?.value,
        code: this.form.get('code')?.value,
        name: this.form.get('name')?.value.trim()
      };

      if (this.idToUpdate) {
        this.languageService.updateLanguage(this.idToUpdate, languageUpdateDTO)
          .subscribe({
            next: (response) => {
              this.updateStatus = {success: true, message: 'Η ενημέρωση ολοκληρώθηκε με επιτυχία! Κάντε Refresh τη σελίδα για να εμφανιστούν οι αλλαγές.'};
              this.onUpdate.emit(this.updateStatus);
            },
            error: (response) => {
              this.updateStatus = {success: false, message: response.error.code};
              this.onUpdate.emit(this.updateStatus);
            }
          })
      }
    }
  }

  onCancel(): void {
    this.setValues();
    this.onCancelUpdate.emit();
  }

  // Getters for the form controls
  get code() {
    return this.form.get('code');
  } 
  get name() {
    return this.form.get('name');
  }
}
