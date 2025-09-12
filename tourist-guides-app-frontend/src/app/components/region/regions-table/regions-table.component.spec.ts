import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegionsTableComponent } from './regions-table.component';

describe('RegionsTableComponent', () => {
  let component: RegionsTableComponent;
  let fixture: ComponentFixture<RegionsTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegionsTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegionsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
