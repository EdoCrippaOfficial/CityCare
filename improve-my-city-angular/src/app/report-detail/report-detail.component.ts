import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable } from 'rxjs';

import { Report } from '../report';
import { ReportService } from '../report.service';
import { Status } from '../status';
import { Category } from '../category';

@Component({
  selector: 'app-report-detail',
  templateUrl: './report-detail.component.html',
  styleUrls: ['./report-detail.component.css']
})
export class ReportDetailComponent implements OnInit {
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

  report: Report;

  constructor(
    private route: ActivatedRoute,
    private reportService: ReportService,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.getReport();
  }

  getReport(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.reportService.getReport(id)
      .subscribe(report => {
        this.report = report;
        this.report.image = this.getImage(report);
      });
  }

  getImage(report: Report): Observable<string | null> {
    return this.reportService.getImageURL(report);
  }

  goBack(): void {
    this.location.back();
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

  onSubmit() {
    this.reportService.updateReport(this.report.id, this.report);
  }

}
