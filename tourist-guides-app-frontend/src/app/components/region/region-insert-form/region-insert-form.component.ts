import { Component, EventEmitter, inject, Output } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Errors } from 'src/app/shared/interfaces/error';
import { RegionInsertDTO } from 'src/app/shared/interfaces/region';
import { RegionService } from 'src/app/shared/services/region.service';

@Component({
  selector: 'app-region-insert-form',
  imports: [ReactiveFormsModule],
  templateUrl: './region-insert-form.component.html',
  styleUrl: './region-insert-form.component.css'
})
export class RegionInsertFormComponent {
  // send the insertStatus to parent component to handle the alerts to be shown to the user
  @Output() onInsert = new EventEmitter<{success: boolean, message: string}>();

  // inform the parent that the button 'Cancel' is clicked
  @Output() onCancelInsert = new EventEmitter<void>();

  regionService: RegionService = inject(RegionService);
  validationErrors: Error[] = Errors;
  insertStatus: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };
  
  // Insert Region Form
  form: FormGroup = new FormGroup({
    name: new FormControl(null, [
      Validators.required,
      Validators.minLength(2)
    ])
  });

  // Check if the input name already exists in DB
  checkNameExists(): void {
    const inputName = this.form.get('name')?.value.trim();

    if (inputName) {
      this.regionService.isNameExists(inputName)
        .subscribe((response) => {
          if (response) {
            this.form.get('name')?.setErrors({regionNameExists: true});
          }
        })
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const regionInsertDTO: RegionInsertDTO = {
        name: this.form.get('name')?.value.trim()
      };

      this.regionService.insertRegion(regionInsertDTO)
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

  // Getters for the form control
  get name() {
    return this.form.get('name');
  }
}
