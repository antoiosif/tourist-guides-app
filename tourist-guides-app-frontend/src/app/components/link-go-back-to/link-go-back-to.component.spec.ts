import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkGoBackToComponent } from './link-go-back-to.component';

describe('LinkGoBackToComponent', () => {
  let component: LinkGoBackToComponent;
  let fixture: ComponentFixture<LinkGoBackToComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LinkGoBackToComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LinkGoBackToComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
