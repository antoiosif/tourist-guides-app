import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuideAccountComponent } from './guide-account.component';

describe('GuideAccountComponent', () => {
  let component: GuideAccountComponent;
  let fixture: ComponentFixture<GuideAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuideAccountComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuideAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
