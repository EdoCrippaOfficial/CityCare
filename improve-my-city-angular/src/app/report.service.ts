import { Injectable } from '@angular/core';
import { Observable } from 'rxjs'; //async stuff
import { map } from 'rxjs/operators';

import { AngularFirestore } from '@angular/fire/firestore';
import { AngularFireStorage } from '@angular/fire/storage';

import { Report } from './report';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  REPORTPATH: string = 'reports';
  IMAGEFOLDER: string = 'images/';
  IMAGENAME: string = '/img';

  reports: Report[];

  constructor(
    private db: AngularFirestore,
    private storage: AngularFireStorage
  ) { }

  getImageURL(report: Report): Observable<string | null> {
    const storageRef = this.storage.ref(this.IMAGEFOLDER + report.image + this.IMAGENAME);
    return storageRef.getDownloadURL();
  }

  getReports(): Observable<Report[]> {
    return this.db.collection<Report>(this.REPORTPATH)
                .snapshotChanges().pipe(map(actions => {
      return actions.map(a => {
        const data = a.payload.doc.data();
        const id = a.payload.doc.id;
        return { id, ...data };
      });
    }));
  }

  getReport(id): Observable<Report> {
    return this.db.doc<Report>(this.REPORTPATH + '/' + id).valueChanges();
  }
}
