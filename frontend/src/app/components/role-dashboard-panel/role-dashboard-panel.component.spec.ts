import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleDashboardPanelComponent } from './role-dashboard-panel.component';

describe('RoleDashboardPanelComponent', () => {
  let component: RoleDashboardPanelComponent;
  let fixture: ComponentFixture<RoleDashboardPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleDashboardPanelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoleDashboardPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
