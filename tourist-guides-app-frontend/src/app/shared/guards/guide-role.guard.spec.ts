import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { guideRoleGuard } from './guide-role.guard';

describe('guideRoleGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => guideRoleGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
