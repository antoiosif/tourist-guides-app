import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuidesListComponent } from './guides-list.component';

describe('GuidesListComponent', () => {
  let component: GuidesListComponent;
  let fixture: ComponentFixture<GuidesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuidesListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuidesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
