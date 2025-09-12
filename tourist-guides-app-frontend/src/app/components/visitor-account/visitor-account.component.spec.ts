import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitorAccountComponent } from './visitor-account.component';

describe('VisitorAccountComponent', () => {
  let component: VisitorAccountComponent;
  let fixture: ComponentFixture<VisitorAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisitorAccountComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VisitorAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
