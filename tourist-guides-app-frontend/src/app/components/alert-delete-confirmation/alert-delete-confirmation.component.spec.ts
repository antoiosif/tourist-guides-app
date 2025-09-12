import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertDeleteConfirmationComponent } from './alert-delete-confirmation.component';

describe('AlertDeleteConfirmationComponent', () => {
  let component: AlertDeleteConfirmationComponent;
  let fixture: ComponentFixture<AlertDeleteConfirmationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertDeleteConfirmationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlertDeleteConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
