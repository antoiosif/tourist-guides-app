import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuidesTableComponent } from './guides-table.component';

describe('GuidesTableComponent', () => {
  let component: GuidesTableComponent;
  let fixture: ComponentFixture<GuidesTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuidesTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuidesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
