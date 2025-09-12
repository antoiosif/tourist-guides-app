import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkGoBackComponent } from './link-go-back.component';

describe('LinkGoBackComponent', () => {
  let component: LinkGoBackComponent;
  let fixture: ComponentFixture<LinkGoBackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LinkGoBackComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LinkGoBackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
