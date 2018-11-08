import { Component, OnInit } from '@angular/core';
import { Report } from '../report';
import { Status } from '../status';
import { REPORTS } from '../mock-report';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  reports = REPORTS;
  accettato = Status.accettato;
  rifiutato = Status.rifiutato;
  attesa = Status.attesa;

  constructor() { }

  ngOnInit() {
  }

}
