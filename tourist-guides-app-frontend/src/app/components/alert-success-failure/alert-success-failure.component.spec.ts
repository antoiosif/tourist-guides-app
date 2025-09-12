import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertSuccessFailureComponent } from './alert-success-failure.component';

describe('AlertSuccessFailureComponent', () => {
  let component: AlertSuccessFailureComponent;
  let fixture: ComponentFixture<AlertSuccessFailureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertSuccessFailureComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlertSuccessFailureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
