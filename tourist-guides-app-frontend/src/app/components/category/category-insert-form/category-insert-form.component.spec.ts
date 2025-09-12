import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryInsertFormComponent } from './category-insert-form.component';

describe('CategoryInsertFormComponent', () => {
  let component: CategoryInsertFormComponent;
  let fixture: ComponentFixture<CategoryInsertFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoryInsertFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CategoryInsertFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
