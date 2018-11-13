import { Component, OnInit } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore';
import { AngularFireStorage } from '@angular/fire/storage';
import { Observable } from 'rxjs'; //async stuff
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Status } from '../status';
import { Report } from '../report';
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

  IMAGEFOLDER: string = 'images/';
  IMAGENAME: string = '/img';
  reportsObservable: Observable<Report[]>;
  reportDetail: Report = new Report();

  constructor(private db: AngularFirestore, private storage: AngularFireStorage,
              private modalService: NgbModal) { }

  ngOnInit() {
    this.reportsObservable = this.getReports('reports');
  }

  getReports(path): Observable<Report[]> {
    return this.db.collection<Report>(path).valueChanges();
  }

  getImageURL(report: Report) {
    const storageRef = this.storage.ref(this.IMAGEFOLDER + report.image + this.IMAGENAME);
    storageRef.getDownloadURL().subscribe(url => {
      report.image = url;
    });
    return report.image;
  }

  showModal(modal, report: Report) {
    this.reportDetail = report;
    this.modalService.open(modal, {ariaLabelledBy: 'modal'});
  }

}
