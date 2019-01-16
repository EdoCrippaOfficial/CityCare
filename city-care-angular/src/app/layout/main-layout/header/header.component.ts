import { Component, OnInit } from '@angular/core';

import { AngularFireAuth } from '@angular/fire/auth';
import * as firebase from 'firebase/app';
import { AuthService } from '../../../auth/auth.service';

import { Status } from '../../../status';
import { Category } from '../../../category';

/**
 * Componente dell'header della pagina di amministrazione
 */
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  /**
   * Per il controllo della proprietà collapsed di bootstrap
   */
  isCollapsed = true;

  /**
   * Username di corrente login
   */
  username;

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
   * Costruttore dell'header della pagina di amministrazione
   */
  constructor(
    public afAuth: AngularFireAuth,
    public authService: AuthService
  ) { }

  /**
   * Alla creazione del componente controlla che il login sia valido tramite database
   *
   * @param 
   * @returns 
   */
  ngOnInit() {
    if (firebase.auth().currentUser) {
      this.afAuth.user.subscribe((user) => {
        this.username = user.email;
      })
    }
  }

  /**
   * Logout dell'utente tramite AuthService
   *
   * @param 
   * @returns 
   */
  doLogout() {
    this.authService.doLogout();
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

}
