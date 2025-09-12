import { Component, inject, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';
import { UserService } from 'src/app/shared/services/user.service';
import { GuideService } from 'src/app/shared/services/guide.service';
import { RegionService } from 'src/app/shared/services/region.service';
import { LanguageService } from 'src/app/shared/services/language.service';
import { GuideReadOnlyDTO } from 'src/app/shared/interfaces/guide';

@Component({
  selector: 'app-guides-table',
  imports: [
    MatIconModule,
    RouterLink
],
  templateUrl: './guides-table.component.html',
  styleUrl: './guides-table.component.css'
})
export class GuidesTableComponent {
  @Input() guides: GuideReadOnlyDTO[] = [];
  userService: UserService = inject(UserService);
  guideService: GuideService = inject(GuideService);
  regionService: RegionService = inject(RegionService);
  languageService: LanguageService = inject(LanguageService);
  router: Router = inject(Router);
  user = this.userService.user$;

  navigateToAccount(uuid: string): void {
    this.router.navigate(['guides', uuid, 'account']);
  }
}
