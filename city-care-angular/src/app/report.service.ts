import { Injectable } from '@angular/core';
import { Observable } from 'rxjs'; //async stuff

import { AngularFirestore } from '@angular/fire/firestore';
import { AngularFireStorage } from '@angular/fire/storage';

import { Report } from './report';

/**
 * Classe che rappresenta il servizio di gestione dei report interfacciandosi con il database
 */
@Injectable({
  providedIn: 'root'
})
export class ReportService {
  /**
   * Path dei report sul database
   */
  REPORTPATH: string = 'reports';
  /**
   * Path delle immagini sul database
   */
  IMAGEFOLDER: string = 'images/';
  /**
   * Suffisso per le immagini sul database
   */
  IMAGENAME: string = '_img';

  /**
   * Array di report ottenuti dal database
   */
  reports: Report[];

  /**
   * Costruttore del servizio di gestione dei report
   */
  constructor(
    private db: AngularFirestore,
    private storage: AngularFireStorage
  ) { }

  /**
   * Ottiene l'url dell'immagine del report passato per parametro
   *
   * @param {Report} report  Report di cui prelevare l'immagine dal database
   * @returns Url dell'immagine ottenuto dal database
   */
  getImageURL(report: Report): Observable<string | null> {
    const storageRef = this.storage.ref(this.IMAGEFOLDER + report.id + this.IMAGENAME);
    return storageRef.getDownloadURL();
  }

  /**
   * Ottiene i report attualmente contenuti nel database
   *
   * @param 
   * @returns Array di report nel database
   */
  getReports(): Observable<Report[]> {
    return this.db.collection<Report>(this.REPORTPATH).valueChanges();
  }

  /**
   * Ottiene un singolo report dal database tramite ID
   *
   * @param {string} id  ID del report
   * @returns Report singolo dal database
   */
  getReport(id: string): Observable<Report> {
    return this.db.doc<Report>(this.REPORTPATH + '/' + id).valueChanges();
  }

  /**
   * Esegue un update sul database del report identificato da ID con report passato come parametro
   *
   * @param {string} id  ID del report
   * @param {Report} report  Report
   * @returns
   */
  updateReport(id: string, report: Report) {
    let thisReport = new Report(report);
    console.log(thisReport);
    this.db.doc<Report>(this.REPORTPATH + '/' + id)
          .update(thisReport.toUploadableObject());
  }
}
