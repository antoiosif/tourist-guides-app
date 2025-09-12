import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LanguageUpdateFormComponent } from './language-update-form.component';

describe('LanguageUpdateFormComponent', () => {
  let component: LanguageUpdateFormComponent;
  let fixture: ComponentFixture<LanguageUpdateFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LanguageUpdateFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LanguageUpdateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
