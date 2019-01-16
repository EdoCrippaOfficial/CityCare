import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

import { AuthService } from '../auth/auth.service'
import { Router } from '@angular/router';

/**
 * Componente del login
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  /**
   * Email inserita nel form di login
   */
  email: string;
  /**
   * Password inserita nel form di login
   */
  password: string;
  /**
   * Stato del login
   */
  loginWrong: boolean = false;

  /**
   * Costruttore del componente di login
   */
  constructor(
    public authService: AuthService,
    private router: Router,
    private titleService: Title) {
      this.titleService.setTitle("City care - Login");
    }

  ngOnInit() {
  }

  /**
   * Ottiene i valori del form di login e inizia la procedura di login tramite AuthService
   *
   * @param 
   * @returns 
   */
  onSubmit() {
    var value = {
      email: this.email,
      password: this.password
    };
    console.log(value)
    this.authService.doLogin(value)
        .then(res => {
          this.router.navigate(['/home']);
        }, err => {
          this.loginWrong = true;
          console.log(err);
        })
  }

}
