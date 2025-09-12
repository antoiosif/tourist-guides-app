import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from '../services/user.service';

export const adminRoleGuard: CanActivateFn = (route, state) => {
  const userService: UserService = inject(UserService);
  const router: Router = inject(Router);
  const hasPermission = userService.user$()?.role === 'SUPER_ADMIN';

  if (userService.user$() && hasPermission) {
    return true;
  }
  return router.navigate(['not-authorized']);
};
