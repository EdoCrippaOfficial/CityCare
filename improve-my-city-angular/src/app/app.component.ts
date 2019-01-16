import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';

/**
 * Componente dell'app Angular
 */
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  public constructor(private titleService: Title) {
    this.titleService.setTitle("City care");
  }
}
