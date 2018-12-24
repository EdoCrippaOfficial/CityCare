import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

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
  accettato = Status.ACCETTATO;
  rifiutato = Status.RIFIUTATO;
  completato = Status.COMPLETATO;
  attesa = Status.ATTESA;

  reports: Report[];
  currentStatus: Status;

  constructor(
    private route: ActivatedRoute,
    private reportService: ReportService
  ) { }

  ngOnInit() {
    this.reportService.getReports()
        .subscribe(reports => {
          this.reports = reports;
          this.reports.forEach(report => {
            report.image = this.getImage(report);
          })
        });
    this.route.params.subscribe(val => {
      this.currentStatus = val.status;
    })
  }

  getImage(report: Report): Observable<string | null> {
    return this.reportService.getImageURL(report);
  }

  getStatus(report: Report): string {
    return Status[report.status];
  }

  isCurrentStatus(report: Report): boolean {
    if (this.currentStatus == null || this.currentStatus == 0) return true;
    else return report.status == this.currentStatus;
  }

  filterReports(reports: Report[]): Report[] {
    if (reports) return reports.filter(report => this.isCurrentStatus(report));
  }

}
