import { Component, EventEmitter, inject, Output } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Errors } from 'src/app/shared/interfaces/error';
import { CategoryInsertDTO } from 'src/app/shared/interfaces/category';
import { CategoryService } from 'src/app/shared/services/category.service';

@Component({
  selector: 'app-category-insert-form',
  imports: [ReactiveFormsModule],
  templateUrl: './category-insert-form.component.html',
  styleUrl: './category-insert-form.component.css'
})
export class CategoryInsertFormComponent {
  // send the insertStatus to parent component to handle the alerts to be shown to the user
  @Output() onInsert = new EventEmitter<{success: boolean, message: string}>();

  // inform the parent that the button 'Cancel' is clicked
  @Output() onCancelInsert = new EventEmitter<void>();

  categoryService: CategoryService = inject(CategoryService);
  validationErrors: Error[] = Errors;
  insertStatus: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };
  
  // Insert Category Form
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
      const categoryInsertDTO: CategoryInsertDTO = {
        name: this.form.get('name')?.value.trim()
      };

      this.categoryService.insertCategory(categoryInsertDTO)
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
