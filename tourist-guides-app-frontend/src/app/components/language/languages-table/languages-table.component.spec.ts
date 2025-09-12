import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LanguagesTableComponent } from './languages-table.component';

describe('LanguagesTableComponent', () => {
  let component: LanguagesTableComponent;
  let fixture: ComponentFixture<LanguagesTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LanguagesTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LanguagesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
