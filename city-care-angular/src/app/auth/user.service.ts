import { Injectable } from "@angular/core";

import { AngularFirestore } from '@angular/fire/firestore';
import { AngularFireAuth } from '@angular/fire/auth';
import * as firebase from 'firebase/app';

/**
 * Classe che rappresenta il servizio di gestione dell'utente
 */
@Injectable()
export class UserService {

  /**
   * Costruttore del servizio di gestione dell'utente
   */
  constructor(
   public db: AngularFirestore,
   public afAuth: AngularFireAuth
 ){ }

  /**
   * Ottiene l'utente corrente
   *
   * @param 
   * @returns Un oggetto Promise<any> che riceve in modo asincrono il risultato dell'ottenimento dell'utente corrente
   */
  getCurrentUser() {
    return new Promise<any>((resolve, reject) => {
      firebase.auth().onAuthStateChanged((user) => {
        if (user) {
          resolve(user);
        } else {
          reject('No user logged in');
        }
      })
    })
  }

  /**
   * Aggiorna l'utente corrente
   *
   * @param 
   * @returns Un oggetto Promise<any> che riceve in modo asincrono il risultato dell'update
   */
  updateCurrentUser(value) {
    return new Promise<any>((resolve, reject) => {
      var user = firebase.auth().currentUser;
      user.updateProfile({
        displayName: value.name,
        photoURL: user.photoURL
      }).then(res => {
        resolve(res)
      }, err => reject(err))
    })
  }
}
