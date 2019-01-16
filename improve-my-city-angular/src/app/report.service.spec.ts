import { TestBed, async } from '@angular/core/testing';

import { ReportService } from './report.service';

import { Report } from './report';

describe('ReportService', () => {
  let service: ReportService;
  beforeEach(() => TestBed.configureTestingModule({ providers: [ReportService]}));

  it('should be created', () => {
    service = TestBed.get(ReportService);
    expect(service).toBeTruthy();
  });

  it('should get observable reports', (done: DoneFn) => {
    service = TestBed.get(ReportService);
    expect(service.getReports()).toBeGreaterThanOrEqual(0);
  });

  it('should get observable report', (done: DoneFn) => {
    service = TestBed.get(ReportService);
    expect(service.getReport('fake')).toBeGreaterThanOrEqual(0);
  });

  it('should get observable string', (done: DoneFn) => {
    service = TestBed.get(ReportService);
    let report: Report;
    expect(service.getImageURL(report)).toBeGreaterThanOrEqual(0);
  });
});
