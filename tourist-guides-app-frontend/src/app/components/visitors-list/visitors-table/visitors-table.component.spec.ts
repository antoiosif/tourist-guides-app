import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisitorsTableComponent } from './visitors-table.component';

describe('VisitorsTableComponent', () => {
  let component: VisitorsTableComponent;
  let fixture: ComponentFixture<VisitorsTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisitorsTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VisitorsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
