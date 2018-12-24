import { Injectable } from '@angular/core';
import { Observable } from 'rxjs'; //async stuff

import { AngularFirestore } from '@angular/fire/firestore';
import { AngularFireStorage } from '@angular/fire/storage';

import { Report } from './report';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  REPORTPATH: string = 'reports';
  IMAGEFOLDER: string = 'images/';
  IMAGENAME: string = '_img';

  reports: Report[];

  constructor(
    private db: AngularFirestore,
    private storage: AngularFireStorage
  ) { }

  getImageURL(report: Report): Observable<string | null> {
    const storageRef = this.storage.ref(this.IMAGEFOLDER + report.id + this.IMAGENAME);
    return storageRef.getDownloadURL();
  }

  getReports(): Observable<Report[]> {
    return this.db.collection<Report>(this.REPORTPATH).valueChanges();
  }

  getReport(id: string): Observable<Report> {
    return this.db.doc<Report>(this.REPORTPATH + '/' + id).valueChanges();
  }

  updateReport(id: string, report: Report) {
    let thisReport = new Report(report);
    this.db.doc<Report>(this.REPORTPATH + '/' + id)
          .update(thisReport.toUploadableObject());
  }
}
