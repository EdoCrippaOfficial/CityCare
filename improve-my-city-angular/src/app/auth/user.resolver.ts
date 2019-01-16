import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Router } from "@angular/router";
import { UserService } from '../auth/user.service';
import { User } from '../auth/user';

/**
 * Classe che rappresenta la risoluzione dell'utente corrente
 */
@Injectable()
export class UserResolver implements Resolve<User> {

  /**
   * Costruttore della classe di risoluzione dell'utente
   */
  constructor(public userService: UserService, private router: Router) { }

  /**
   * Risolve l'utente corrente
   *
   * @param {ActivatedRouteSnapshot} route  Istanza dell'attuale routing
   * @returns Un oggetto Promise<User> che riceve in modo asincrono il risultato della risoluzione dell'utente
   */
  resolve(route: ActivatedRouteSnapshot) : Promise<User> {

    let user = new User();

    return new Promise((resolve, reject) => {
      this.userService.getCurrentUser()
      .then(res => {
        if(res.providerData[0].providerId == 'password'){
          user.username = res.displayName;
          return resolve(user);
        }
        else{
          user.username = res.displayName;
          return resolve(user);
        }
      }, err => {
        this.router.navigate(['/login']);
        return reject(err);
      })
    })
  }
}
