import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';

import { ReportService } from '../report.service';
import { Report } from '../report';
import { Status } from '../status';
//import { REPORTS } from '../mock-report';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  accettato = Status.accettato;
  rifiutato = Status.rifiutato;
  completato = Status.completato;
  attesa = Status.attesa;

  reports: Report[];

  constructor(
    private reportService: ReportService
  ) { }

  ngOnInit() {
    this.reportService.getReports()
        .subscribe(reports => {
          this.reports = reports;
          reports.forEach(report => {
            report.image = this.getImage(report);
          })
        });
  }

  getImage(report: Report): Observable<string | null> {
    return this.reportService.getImageURL(report);
  }

}
