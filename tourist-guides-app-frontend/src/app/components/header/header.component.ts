import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { MatIconModule } from '@angular/material/icon';
import { UserService } from 'src/app/shared/services/user.service';

@Component({
  selector: 'app-header',
  imports: [
    RouterLink, 
    RouterLinkActive, 
    NgbCollapseModule, 
    NgbDropdownModule, 
    MatIconModule
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  userService: UserService = inject(UserService);
  router: Router = inject(Router);
  
  user = this.userService.user$;
  isMenuCollapsed: boolean = true;

  logout(): void {
    this.userService.logoutUser();
  }

  navigateToAccount(): void {
    if (this.user()?.role === 'GUIDE') {
      this.router.navigate(['guides', this.user()?.uuid, 'account']);
    } else if (this.user()?.role === 'VISITOR') {
      this.router.navigate(['visitors', this.user()?.uuid, 'account']);
    }
  }

  navigateToFavorites(): void {
    this.router.navigate(['guides', this.user()?.uuid, 'favorites']);
  }
}
