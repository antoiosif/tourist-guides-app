import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuideRegisterComponent } from './guide-register.component';

describe('GuideRegisterComponent', () => {
  let component: GuideRegisterComponent;
  let fixture: ComponentFixture<GuideRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuideRegisterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuideRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
