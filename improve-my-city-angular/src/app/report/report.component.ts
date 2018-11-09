import { Component, OnInit } from '@angular/core';
import { AngularFireDatabase } from '@angular/fire/database';
import { Observable } from 'rxjs'; //async stuff

import { Status } from '../status';
//import { REPORTS } from '../mock-report';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  //reports = REPORTS;
  accettato = Status.accettato;
  rifiutato = Status.rifiutato;
  attesa = Status.attesa;

  reportsObservable: Observable<any[]>;
  constructor(private db: AngularFireDatabase) { }

  ngOnInit() {
    this.reportsObservable = this.getReports('/Posts');
  }

  getReports(path): Observable<any[]> {
    return this.db.list(path).valueChanges();
  }

}
