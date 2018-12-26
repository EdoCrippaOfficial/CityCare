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
  ordinaTipo: string;
  ordinaVerso: string;

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

  sortReports(reports: Report[]): Report[] {
    if (reports) {
      if (this.ordinaTipo == "data")
        reports.sort((a,b) => (a.timestamp > b.timestamp) ? 1 : (a.timestamp === b.timestamp) ? 0 : -1);
      else
        reports.sort((a,b) => (a.n_stars > b.n_stars) ? 1 : (a.n_stars === b.n_stars) ? 0 : -1);
      if (this.ordinaVerso == "desc") reports.reverse();
      return this.filterReports(reports);
    }
  }

  onSubmit(reports: Report[]) {
    if (reports) this.reports = this.sortReports(reports);
  }

}
