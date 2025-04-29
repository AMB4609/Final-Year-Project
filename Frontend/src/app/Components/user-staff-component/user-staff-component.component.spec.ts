import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserStaffComponentComponent } from './user-staff-component.component';

describe('UserStaffComponentComponent', () => {
  let component: UserStaffComponentComponent;
  let fixture: ComponentFixture<UserStaffComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserStaffComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserStaffComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
