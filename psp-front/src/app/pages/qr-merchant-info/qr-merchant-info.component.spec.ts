import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QrMerchantInfoComponent } from './qr-merchant-info.component';

describe('QrMerchantInfoComponent', () => {
  let component: QrMerchantInfoComponent;
  let fixture: ComponentFixture<QrMerchantInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QrMerchantInfoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QrMerchantInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
