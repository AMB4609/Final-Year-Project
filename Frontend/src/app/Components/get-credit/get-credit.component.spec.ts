import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetCreditComponent } from './get-credit.component';

describe('GetCreditComponent', () => {
  let component: GetCreditComponent;
  let fixture: ComponentFixture<GetCreditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GetCreditComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GetCreditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
