import { Component, EventEmitter, inject, Input, OnChanges, Output } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Errors } from 'src/app/shared/interfaces/error';
import { CategoryReadOnlyDTO, CategoryUpdateDTO } from 'src/app/shared/interfaces/category';
import { CategoryService } from 'src/app/shared/services/category.service';

@Component({
  selector: 'app-category-update-form',
  imports: [ReactiveFormsModule],
  templateUrl: './category-update-form.component.html',
  styleUrl: './category-update-form.component.css'
})
export class CategoryUpdateFormComponent implements OnChanges {
  // send the updateStatus to parent component to handle the alerts to be shown to the user
  @Output() onUpdate = new EventEmitter<{success: boolean, message: string}>();

  // inform the parent that the button 'Cancel' is clicked
  @Output() onCancelUpdate = new EventEmitter<void>();

  @Input() idToUpdate: bigint | undefined;
  categoryService: CategoryService = inject(CategoryService);
  validationErrors: Error[] = Errors;
  categoryToUpdate: CategoryReadOnlyDTO | null = null;
  updateStatus: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };
  
  // Update Category Form
  form: FormGroup = new FormGroup({
    id: new FormControl({value: null, disabled: true}),
    name: new FormControl(null, [
      Validators.required,
      Validators.minLength(2)
    ])
  });

  ngOnChanges(): void {
    // Get Category
    if (this.idToUpdate) {
      this.categoryService.getCategoryById(this.idToUpdate)
        .subscribe({
          next: (response) => {
            this.categoryToUpdate = response;
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
      id: this.categoryToUpdate?.id,
      name: this.categoryToUpdate?.name,
    });
  }

  // Check if the input name already exists in DB and that the input value is not the same with the value of the Category to update
  checkNameExists(): void {
    const inputName = this.form.get('name')?.value.trim();

    if (inputName && inputName !== this.categoryToUpdate?.name) {
      this.categoryService.isNameExists(inputName)
        .subscribe((response) => {
          if (response) {
            this.form.get('name')?.setErrors({categoryNameExists: true});
          }
        })
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const categoryUpdateDTO: CategoryUpdateDTO = {
        id: this.form.get('id')?.value,
        name: this.form.get('name')?.value.trim()
      };

      if (this.idToUpdate) {
        this.categoryService.updateCategory(this.idToUpdate, categoryUpdateDTO)
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
