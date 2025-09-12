import { Component, EventEmitter, inject, Input, OnChanges, Output } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Errors } from 'src/app/shared/interfaces/error';
import { RegionReadOnlyDTO, RegionUpdateDTO } from 'src/app/shared/interfaces/region';
import { RegionService } from 'src/app/shared/services/region.service';

@Component({
  selector: 'app-region-update-form',
  imports: [ReactiveFormsModule],
  templateUrl: './region-update-form.component.html',
  styleUrl: './region-update-form.component.css'
})
export class RegionUpdateFormComponent implements OnChanges {
  // send the updateStatus to parent component to handle the alerts to be shown to the user
  @Output() onUpdate = new EventEmitter<{success: boolean, message: string}>();

  // inform the parent that the button 'Cancel' is clicked
  @Output() onCancelUpdate = new EventEmitter<void>();

  @Input() idToUpdate: bigint | undefined;
  regionService: RegionService = inject(RegionService);
  validationErrors: Error[] = Errors;
  regionToUpdate: RegionReadOnlyDTO | null = null;
  updateStatus: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };
  
  // Update Region Form
  form: FormGroup = new FormGroup({
    id: new FormControl({value: null, disabled: true}),
    name: new FormControl(null, [
      Validators.required,
      Validators.minLength(2)
    ])
  });

  ngOnChanges(): void {
    // Get Region
    if (this.idToUpdate) {
      this.regionService.getRegionById(this.idToUpdate)
        .subscribe({
          next: (response) => {
            this.regionToUpdate = response;
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
      id: this.regionToUpdate?.id,
      name: this.regionToUpdate?.name,
    });
  }

  // Check if the input name already exists in DB and that the input value is not the same with the value of the Region to update
  checkNameExists(): void {
    const inputName = this.form.get('name')?.value.trim();

    if (inputName && inputName !== this.regionToUpdate?.name) {
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
      const regionUpdateDTO: RegionUpdateDTO = {
        id: this.form.get('id')?.value,
        name: this.form.get('name')?.value.trim()
      };

      if (this.idToUpdate) {
        this.regionService.updateRegion(this.idToUpdate, regionUpdateDTO)
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

  // Getters for the form control
  get name() {
    return this.form.get('name');
  }
}
