import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable } from 'rxjs';
import { Title } from '@angular/platform-browser';

import { Report } from '../report';
import { ReportService } from '../report.service';
import { Status } from '../status';
import { Category } from '../category';

/**
 * Componente del dettaglio di un report
 */
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
  /**
   * Lista di enumerativi per lo stato
   */
  statusList = [
    this.accettato,
    this.rifiutato,
    this.completato,
    this.attesa
  ];

  strada = Category.MANUTENZIONESTRADALE;
  elettrica = Category.MANUTENZIONEELETTRICA;
  giardinaggio = Category.GIARDINAGGIO;
  polizia = Category.POLIZIALOCALE;
  /**
   * Lista di enumerativi per la categoria
   */
  categoryList = [
    this.strada,
    this.elettrica,
    this.giardinaggio,
    this.polizia
  ]

  /**
   * Report corrente
   */
  report: Report;
  /**
   * Status inserito nel form
   */
  formStatus: Status;
  /**
   * Categoria inserita nel form
   */
  formCategory: Category;

  /**
   * Costruttore per il componente dettaglio report
   */
  constructor(
    private route: ActivatedRoute,
    private reportService: ReportService,
    private location: Location,
    private titleService: Title) {
      this.titleService.setTitle("City care - Dettaglio report");
    }

  /**
   * Alla creazione chiama la funzione getReport()
   *
   * @param 
   * @returns 
   */
  ngOnInit(): void {
    this.getReport();
  }

  /**
   * Ottiene i dati relativi al report interessato tramite ReportService, usando l'ID del report nella url attuale
   *
   * @param 
   * @returns
   */
  getReport(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.reportService.getReport(id)
      .subscribe(report => {
        this.report = report;
        this.report.image = this.getImage(report);
      });
  }

  /**
   * Ottiene l'immagine del report tramite ReportService
   *
   * @param {Report} report  Report
   * @returns Url dell'immagine del report
   */
  getImage(report: Report): Observable<string | null> {
    return this.reportService.getImageURL(report);
  }

  /**
   * Torna alla pagina da cui si è arrivati
   *
   * @param 
   * @returns
   */
  goBack(): void {
    this.location.back();
  }

  /**
   * Ottiene una rappresentazione testuale dello status in input
   *
   * @param {any} status  status
   * @returns Rappresentazione testuale dello stato
   */
  getStatusText(status: any): string {
    let istatus;

    if (typeof status == "string") istatus = Number.parseInt(status);
    else istatus = status;

    switch (istatus) {
      case Status.ACCETTATO: return 'Accettato'; break;
      case Status.RIFIUTATO: return 'Rifiutato'; break;
      case Status.COMPLETATO: return 'Completato'; break;
      case Status.ATTESA: return 'In attesa'; break;
      default: return 'Sconosciuto'; break;
    }
  }

  /**
   * Ottiene una rappresentazione testuale della categoria in input
   *
   * @param {any} category  categoria
   * @returns Rappresentazione testuale della categoria
   */
  getCategoryText(category: any): string {
    let icategory;

    if (typeof category == "string") icategory = Number.parseInt(category);
    else icategory = category;

    switch (icategory) {
      case Category.MANUTENZIONESTRADALE: return 'Manutenzione Stradale'; break;
      case Category.MANUTENZIONEELETTRICA: return 'Manutenzione Elettrica'; break;
      case Category.GIARDINAGGIO: return 'Giardinaggio'; break;
      case Category.POLIZIALOCALE: return 'Polizia Locale'; break;
      default: return 'Altro'; break;
    }
  }

  /**
   * Aggiorna i dati del report sul database tramite ReportService al click del pulsante invia
   *
   * @param 
   * @returns 
   */
  onSubmit() {
    this.reportService.updateReport(this.report.id, this.report);
  }

}
