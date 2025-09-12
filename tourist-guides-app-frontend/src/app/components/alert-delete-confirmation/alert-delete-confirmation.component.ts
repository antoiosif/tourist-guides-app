import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-alert-delete-confirmation',
  imports: [NgbAlertModule],
  templateUrl: './alert-delete-confirmation.component.html',
  styleUrl: './alert-delete-confirmation.component.css'
})
export class AlertDeleteConfirmationComponent  {
  // inform the parent that the button of confirm Delete is clicked
  @Output() onDelete = new EventEmitter<void>();

  // inform the parent that the button 'Cancel' is clicked
  @Output() onCancelDelete = new EventEmitter<void>();

  @Input() text1: string | null = null;
  @Input() text2: string | null = null;
  @Input() idToDelete: bigint | string | null = null;

  confirmDelete(): void {
    this.onDelete.emit();
  }

  cancelDelete(): void {
    this.onCancelDelete.emit();
  }
}
