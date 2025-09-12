import { Component, inject, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { UserService } from 'src/app/shared/services/user.service';
import { VisitorService } from 'src/app/shared/services/visitor.service';
import { RegionService } from 'src/app/shared/services/region.service';
import { VisitorReadOnlyDTO } from 'src/app/shared/interfaces/visitor';

@Component({
  selector: 'app-visitors-table',
  imports: [
    MatIconModule
  ],
  templateUrl: './visitors-table.component.html',
  styleUrl: './visitors-table.component.css'
})
export class VisitorsTableComponent {
  @Input() visitors: VisitorReadOnlyDTO[] = [];
  userService: UserService = inject(UserService);
  visitorService: VisitorService = inject(VisitorService);
  regionService: RegionService = inject(RegionService);
  router: Router = inject(Router);
  user = this.userService.user$

  navigateToAccount(uuid: string): void {
    this.router.navigate(['visitors', uuid, 'account']);
  }
}
