import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MerchantInfoPageComponent } from './merchant-info-page.component';

describe('MerchantInfoPageComponent', () => {
  let component: MerchantInfoPageComponent;
  let fixture: ComponentFixture<MerchantInfoPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MerchantInfoPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MerchantInfoPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
