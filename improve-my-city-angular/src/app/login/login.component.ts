import { Component, OnInit } from '@angular/core';

import { AuthService } from '../auth/auth.service'
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  email: string;
  password: string;

  constructor(
    public authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
  }

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
          console.log(err);
        })
  }

}
