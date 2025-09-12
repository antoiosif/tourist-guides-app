import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuideFavoritesComponent } from './guide-favorites.component';

describe('GuideFavoritesComponent', () => {
  let component: GuideFavoritesComponent;
  let fixture: ComponentFixture<GuideFavoritesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuideFavoritesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuideFavoritesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
