import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegionInsertFormComponent } from './region-insert-form.component';

describe('RegionInsertFormComponent', () => {
  let component: RegionInsertFormComponent;
  let fixture: ComponentFixture<RegionInsertFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegionInsertFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegionInsertFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
