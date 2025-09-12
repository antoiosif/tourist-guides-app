import { Component, Input } from '@angular/core';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { BackendErrors } from 'src/app/shared/interfaces/error';

@Component({
  selector: 'app-alert-success-failure',
  imports: [NgbAlertModule],
  templateUrl: './alert-success-failure.component.html',
  styleUrl: './alert-success-failure.component.css'
})
export class AlertSuccessFailureComponent {
  @Input() status: {success: boolean, message: string} = {
    success: false,
    message: 'Not attempted yet'
  }

  backendErrors: Error[] = BackendErrors;

  closeAlert() {
    this.status = {
      success: false,
      message: 'Not attempted yet'
      }
  }
}
