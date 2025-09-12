import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RegionService } from 'src/app/shared/services/region.service';

@Component({
  selector: 'app-regions-table',
  imports: [MatIconModule],
  templateUrl: './regions-table.component.html',
  styleUrl: './regions-table.component.css'
})
export class RegionsTableComponent {
  // send to parent component the id of the Region to be updated
  @Output() onClickUpdate = new EventEmitter<bigint>();

  // send to parent component the id of the Region to be deleted
  @Output() onClickDelete = new EventEmitter<bigint>();
  
  @Input() isInsertFormCollapsed: boolean | undefined;
  @Input() isUpdateFormCollapsed: boolean | undefined;
  @Input() isConfirmationAlertVisible: boolean | undefined;
  regionService: RegionService = inject(RegionService);
  regions = this.regionService.regions$;

  onUpdate(id: bigint) {
    this.onClickUpdate.emit(id);
  }

  onDelete(id: bigint) {
    this.onClickDelete.emit(id);
  }
}
