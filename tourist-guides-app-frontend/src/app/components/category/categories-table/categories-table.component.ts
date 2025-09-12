import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { CategoryService } from 'src/app/shared/services/category.service';

@Component({
  selector: 'app-categories-table',
  imports: [MatIconModule],
  templateUrl: './categories-table.component.html',
  styleUrl: './categories-table.component.css'
})
export class CategoriesTableComponent {
  // send to parent component the id of the Category to be updated
  @Output() onClickUpdate = new EventEmitter<bigint>();

  // send to parent component the id of the Category to be deleted
  @Output() onClickDelete = new EventEmitter<bigint>();

  @Input() isInsertFormCollapsed: boolean | undefined;
  @Input() isUpdateFormCollapsed: boolean | undefined;
  @Input() isConfirmationAlertVisible: boolean | undefined;
  categoryService: CategoryService = inject(CategoryService);
  categories = this.categoryService.categories$;

  onUpdate(id: bigint) {
    this.onClickUpdate.emit(id);
  }

  onDelete(id: bigint) {
    this.onClickDelete.emit(id);
  }
}
