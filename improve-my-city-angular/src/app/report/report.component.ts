import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';

import { ReportService } from '../report.service';
import { Report } from '../report';
import { Status } from '../status';
import { Category } from '../category';

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
  statusList = [
    this.accettato,
    this.rifiutato,
    this.completato,
    this.attesa
  ];

  a = Category.A;
  b = Category.B;
  c = Category.C;
  d = Category.D;
  categoryList = [
    this.a,
    this.b,
    this.c,
    this.d
  ]

  reports: Report[];
  filteredReports: Report[];
  currentStatus: Status;
  currentCategory: Category;

  formStatus: Status;
  formCategory: Category;
  ordinaTipo: string;
  ordinaVerso: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private reportService: ReportService
  ) { }

  ngOnInit() {
    this.reportService.getReports()
        .subscribe(reports => {
          this.reports = reports;
          this.reports.forEach(report => {
            report.image = this.getImage(report);
          })
          this.filteredReports = this.reports;
        });
    this.route.params.subscribe(val => {
      this.currentStatus = val.status;
      this.currentCategory = val.category;
    })
  }

  getImage(report: Report): Observable<string | null> {
    return this.reportService.getImageURL(report);
  }

  getStatus(report: Report): string {
    return Status[report.status];
  }

  getCategory(report: Report): string {
    return Category[report.category];
  }

  getStatusText(status: number): string {
    return Status[status];
  }

  getCategoryText(category: number): string {
    return Category[category];
  }

  isCurrentStatus(report: Report): boolean {
    if (this.currentStatus == null || this.currentStatus == 0) return true;
    else return report.status == this.currentStatus;
  }

  isCurrentCategory(report: Report): boolean {
    if (this.currentCategory == null || this.currentCategory == 0) return true;
    else return report.category == this.currentCategory;
  }

  filterReports(reports: Report[]): Report[] {
    if (reports) return reports.filter(report =>
      this.isCurrentStatus(report) && this.isCurrentCategory(report));
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

  onSubmit() {
    if (this.reports) {
      if (this.ordinaTipo != null || this.ordinaVerso != null)
          this.filteredReports = this.sortReports(this.reports);
      let status = this.formStatus == null ? 0 : this.formStatus.toString();
      let category = this.formCategory == null ? 0 : this.formCategory.toString();
      let path = 'reports/' + status + '/' + category;
      console.log(this.filteredReports);
      this.router.navigate([path]);
    }
  }

}
