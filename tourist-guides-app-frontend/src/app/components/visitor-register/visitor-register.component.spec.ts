import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitorRegisterComponent } from './visitor-register.component';

describe('VisitorRegisterComponent', () => {
  let component: VisitorRegisterComponent;
  let fixture: ComponentFixture<VisitorRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisitorRegisterComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VisitorRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
