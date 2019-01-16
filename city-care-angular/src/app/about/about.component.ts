import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

/**
 * Componente della pagina about
 */
@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {

  /**
   * Costruttore della pagina about
   */
  constructor(private titleService: Title) {
    this.titleService.setTitle("City care - About");
  }

  ngOnInit() {
  }

}
