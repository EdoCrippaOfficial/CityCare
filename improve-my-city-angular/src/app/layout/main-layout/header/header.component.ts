import { Component, OnInit } from '@angular/core';

import { AngularFireAuth } from '@angular/fire/auth';
import * as firebase from 'firebase/app';
import { AuthService } from '../../../auth/auth.service';

import { Status } from '../../../status';
import { Category } from '../../../category';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  isCollapsed = true;

  username;

  accettato = Status.ACCETTATO;
  rifiutato = Status.RIFIUTATO;
  completato = Status.COMPLETATO;
  attesa = Status.ATTESA;

  a = Category.A;
  b = Category.B;
  c = Category.C;
  d = Category.D;

  constructor(
    public afAuth: AngularFireAuth,
    public authService: AuthService
  ) { }

  ngOnInit() {
    if (firebase.auth().currentUser) {
      this.afAuth.user.subscribe((user) => {
        this.username = user.email;
      })
    }
  }

  doLogout() {
    this.authService.doLogout();
  }

}
