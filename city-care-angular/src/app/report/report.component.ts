import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

import { ReportService } from '../report.service';
import { Report } from '../report';
import { Status } from '../status';
import { Category } from '../category';

/**
 * Componente della lista dei report
 */
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
   * Lista dei report
   */
  reports: Report[];
  /**
   * Lista dei report filtrati
   */
  filteredReports: Report[];
  /**
   * Status correntemente visualizzato
   */
  currentStatus: Status;
  /**
   * Categoria correntemente visualizzata
   */
  currentCategory: Category;

  /**
   * Status nel form di ricerca
   */
  formStatus: Status;
  /**
   * Categoria nel form di ricerca
   */
  formCategory: Category;
  /**
   * Tipo di ordine nel form di ricerca
   */
  ordinaTipo: string;
  /**
   * Verso di ordine nel form di ricerca
   */
  ordinaVerso: string;

  /**
   * Costruttore del componente della lista dei report
   */
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private reportService: ReportService,
    private titleService: Title) {
      this.titleService.setTitle("City care - Reports");
    }

  /**
   * Alla creazione chiama la funzione getReports() di ReportService per ottenere la lista dei report nel database e ottiene lo stato e la categoria correnti
   *
   * @param 
   * @returns 
   */
  ngOnInit() {
    this.reportService.getReports()
        .subscribe(reports => {
          this.reports = reports;
          reports.sort((a,b) => (this.getPriority(a) < this.getPriority(b)) ? 1 : -1);
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

  /**
   * Ottiene la priorità del report. La priorità è calcolata come una media pesata tra il numero di stelle del report e il tempo di permanenza in database prima di essere completato
   *
   * @param {Report} report  report
   * @returns Priorità del report in input
   */
  getPriority(report: Report): number {
    if (report.status == this.completato) return 0;
    return (report.n_stars * 5 + Math.ceil((Date.now() - new Date(report.timestamp).getTime())/(1000 * 3600 * 24)))/6;
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
   * Verifica se il report è quello richiesto dalla visualizzazione corrente in termini di status
   *
   * @param {Report} report  report
   * @returns Vero se il report ha lo status corrente, falso altrimenti
   */
  isCurrentStatus(report: Report): boolean {
    if (this.currentStatus == null || this.currentStatus == 0) return true;
    else return report.status == this.currentStatus;
  }

  /**
   * Verifica se il report è quello richiesto dalla visualizzazione corrente in termini di categoria
   *
   * @param {Report} report  report
   * @returns Vero se il report ha la categoria corrente, falso altrimenti
   */
  isCurrentCategory(report: Report): boolean {
    if (this.currentCategory == null || this.currentCategory == 0) return true;
    else return report.category == this.currentCategory;
  }

  /**
   * Funzione di filtraggio dei report
   *
   * @param {Report[]} reports  Array di report
   * @returns 
   */
  filterReports(reports: Report[]): Report[] {
    if (reports) return reports.filter(report =>
      this.isCurrentStatus(report) && this.isCurrentCategory(report));
  }

  /**
   * Ordina i report in base alla richiesta della ricerca
   *
   * @param {Report[]} reports  Array di report
   * @returns Report ordinati e filtrati in base alla richiesta della ricerca
   */
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

  /**
   * Al click di invia, ottiene i dati dal form di ricerca e filtra i report di conseguenza
   *
   * @param 
   * @returns 
   */
  onSubmit() {
    if (this.reports) {
      if (this.ordinaTipo != null || this.ordinaVerso != null)
          this.filteredReports = this.sortReports(this.reports);
      let status = this.formStatus == null ? 0 : this.formStatus.toString();
      let category = this.formCategory == null ? 0 : this.formCategory.toString();
      let path = 'reports/' + status + '/' + category;
      this.router.navigate([path]);
    }
  }

}
