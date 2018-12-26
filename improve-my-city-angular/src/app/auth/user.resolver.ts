import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Router } from "@angular/router";
import { UserService } from '../auth/user.service';
import { User } from '../auth/user';

@Injectable()
export class UserResolver implements Resolve<User> {

  constructor(public userService: UserService, private router: Router) { }

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
