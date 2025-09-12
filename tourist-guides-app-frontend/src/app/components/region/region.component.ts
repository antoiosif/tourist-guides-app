import { Component, inject } from '@angular/core';
import { ViewportScroller } from '@angular/common'
import { MatIconModule } from '@angular/material/icon';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { BackendErrors } from 'src/app/shared/interfaces/error';
import { RegionsTableComponent } from "./regions-table/regions-table.component";
import { RegionInsertFormComponent } from './region-insert-form/region-insert-form.component';
import { RegionUpdateFormComponent } from './region-update-form/region-update-form.component';
import { RegionService } from 'src/app/shared/services/region.service';
import { LinkGoBackComponent } from "../link-go-back/link-go-back.component";
import { AlertSuccessFailureComponent } from "../alert-success-failure/alert-success-failure.component";
import { AlertDeleteConfirmationComponent } from "../alert-delete-confirmation/alert-delete-confirmation.component";

@Component({
  selector: 'app-region',
  imports: [
    MatIconModule,
    NgbCollapseModule, 
    RegionsTableComponent, 
    RegionInsertFormComponent,
    RegionUpdateFormComponent,
    LinkGoBackComponent,
    AlertSuccessFailureComponent,
    AlertDeleteConfirmationComponent
  ],
  templateUrl: './region.component.html',
  styleUrl: './region.component.css'
})
export class RegionComponent {
  regionService: RegionService = inject(RegionService);
  viewportScroller: ViewportScroller = inject(ViewportScroller);

  backendErrors: Error[] = BackendErrors;
  idToUpdate: bigint | undefined;
  idToDelete: bigint | null = null;
  isConfirmationAlertVisible: boolean = false;
  isInsertFormCollapsed: boolean = true;
  isUpdateFormCollapsed: boolean = true;
  status: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  };

  // for Insert - Insert is done in InsertFormComponent and this component only handles the alerts shown to the user
  handleInsertAlerts(insertStatus: {success: boolean, message: string}): void {
    this.status = insertStatus;
    if (this.status.success) {
      this.isInsertFormCollapsed = true;
    }
  }

  // for Update - Update is done in UpdateFormComponent and this component takes the id of the region to be updated from the TableComponent, sends it to UpdateFormComponent and then handles the alerts shown to the user
  setToUpdate(id: bigint) {
    this.idToUpdate = id;
    this.isUpdateFormCollapsed = !this.isUpdateFormCollapsed; // show the Update Form
    this.scrollToTop(); // scroll to the top of page where the form is shown
  }

  handleUpdateAlerts(updateStatus: {success: boolean, message: string}): void {
    this.status = updateStatus;
    if (this.status.success) {
      this.isUpdateFormCollapsed = true;
    }
  }

  // for Delete - Delete is done in this component after receiving the id of the region to be deleted from the TableComponent, and then handles the alerts shown to the user
  setToDelete(id: bigint): void {
    this.idToDelete = id;
    this.isConfirmationAlertVisible = true;
    this.scrollToTop(); // scroll to the top of page where the alert is shown
  }

  proceedToDelete(id: bigint | null): void {
    if (id) {
      this.regionService.deleteRegion(id)
        .subscribe({
            next: (response) => {
              this.status = {success: true, message: 'Η διαγραφή ολοκληρώθηκε με επιτυχία! Κάντε Refresh τη σελίδα για να εμφανιστούν οι αλλαγές.'};
            },
            error: (response) => {
              this.status = {success: false, message: response.error.code};
            }
          })
      this.isConfirmationAlertVisible = false;  // set to 'false' for the confirmation alert to disappear
    }
  }

  cancelDelete(): void {
    this.idToDelete = null;
    this.isConfirmationAlertVisible = false;
  }

  scrollToTop() {
    this.viewportScroller.scrollToPosition([0, 0]);
  }
}
