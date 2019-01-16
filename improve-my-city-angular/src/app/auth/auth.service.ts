import { Injectable } from "@angular/core";
import { AngularFireAuth } from '@angular/fire/auth';
import * as firebase from 'firebase/app';

/**
 * Classe che gestisce i servizi di autorizzazione per l'amministrazione
 */
@Injectable()
export class AuthService {

  /**
   * Costruttore del servizio di autorizzazione per l'amministrazione
   */
  constructor(
   public afAuth: AngularFireAuth
 ){}

  /**
   * Esegue il login dell'utente passato in input
   *
   * @param {any} value  Dati di login dell'utente
   * @returns Un oggetto Promise<any> che riceve in modo asincrono il risultato del login
   */
  doLogin(value) {
    return new Promise<any>((resolve, reject) => {
      firebase.auth().signInWithEmailAndPassword(value.email, value.password)
      .then(res => {
        resolve(res);
        setTimeout(() => {
          firebase.auth().signOut()
        }, 1000 * 3600)
      }, err => reject(err))
    })
  }

  /**
   * Esegue il logout dell'utente corrente
   *
   * @param 
   * @returns Un oggetto Promise che riceve in modo asincrono il risultato del logout
   */
  doLogout() {
    return new Promise((resolve, reject) => {
      if (firebase.auth().currentUser) {
        this.afAuth.auth.signOut();
        resolve();
      } else {
        reject();
      }
    });
  }

}
