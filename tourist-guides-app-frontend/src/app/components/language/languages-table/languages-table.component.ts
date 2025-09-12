import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { LanguageService } from 'src/app/shared/services/language.service';

@Component({
  selector: 'app-languages-table',
  imports: [MatIconModule],
  templateUrl: './languages-table.component.html',
  styleUrl: './languages-table.component.css'
})
export class LanguagesTableComponent {
  // send to parent component the id of the Language to be updated
  @Output() onClickUpdate = new EventEmitter<bigint>();

  // send to parent component the id of the Language to be deleted
  @Output() onClickDelete = new EventEmitter<bigint>();
  
  @Input() isInsertFormCollapsed: boolean | undefined;
  @Input() isUpdateFormCollapsed: boolean | undefined;
  @Input() isConfirmationAlertVisible: boolean | undefined;
  languageService: LanguageService = inject(LanguageService);
  languages = this.languageService.languages$;

  onUpdate(id: bigint) {
    this.onClickUpdate.emit(id);
  }

  onDelete(id: bigint) {
    this.onClickDelete.emit(id);
  }
}
