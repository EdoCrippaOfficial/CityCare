import { Injectable } from '@angular/core';
import { CanActivate, Router } from "@angular/router";
import { AngularFireAuth } from '@angular/fire/auth';
import { UserService } from '../auth/user.service';

/**
 * Classe guardia per l'autorizzazione
 */
@Injectable()
export class AuthGuard implements CanActivate {

  /**
   * Costruzione della classe guardia per l'autorizzazione
   */
  constructor (
    public afAuth: AngularFireAuth,
    public userService: UserService,
    private router: Router
  ) {}

  /**
   * Ottiene l'url dell'immagine del report passato per parametro
   *
   * @param {Report} report  Report di cui prelevare l'immagine dal database
   * @returns Un oggetto Promise<boolean> che riceve in modo asincrono Vero se un utente può fare routing sull'indirizzo corrente, Falso altrimenti e viene riportato alla pagina di login tramite home
   */
  canActivate(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      this.userService.getCurrentUser()
      .then(user => {
        this.router.navigate(['/home']);
        return resolve(false);
      }, err => {
        return resolve(true);
      })
    })
  }
}
