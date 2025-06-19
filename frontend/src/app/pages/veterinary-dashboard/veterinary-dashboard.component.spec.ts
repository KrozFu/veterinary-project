import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VeterinaryDashboardComponent } from './veterinary-dashboard.component';

describe('VeterinaryDashboardComponent', () => {
  let component: VeterinaryDashboardComponent;
  let fixture: ComponentFixture<VeterinaryDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VeterinaryDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VeterinaryDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
