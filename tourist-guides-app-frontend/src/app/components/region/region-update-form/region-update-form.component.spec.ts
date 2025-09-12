import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegionUpdateFormComponent } from './region-update-form.component';

describe('RegionUpdateFormComponent', () => {
  let component: RegionUpdateFormComponent;
  let fixture: ComponentFixture<RegionUpdateFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegionUpdateFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegionUpdateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
