import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LanguageInsertFormComponent } from './language-insert-form.component';

describe('LanguageInsertFormComponent', () => {
  let component: LanguageInsertFormComponent;
  let fixture: ComponentFixture<LanguageInsertFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LanguageInsertFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LanguageInsertFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
