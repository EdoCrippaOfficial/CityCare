import { Injectable } from "@angular/core";
import { AngularFireAuth } from '@angular/fire/auth';
import * as firebase from 'firebase/app';

@Injectable()
export class AuthService {

  constructor(
   public afAuth: AngularFireAuth
 ){}

  doLogin(value) {
    return new Promise<any>((resolve, reject) => {
      firebase.auth().signInWithEmailAndPassword(value.email, value.password)
      .then(res => {
        resolve(res);
        setTimeout(() => {
          firebase.auth().signOut().then(() => {
            console.log('out');
          })
        }, 3600)
      }, err => reject(err))
    })
  }

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
