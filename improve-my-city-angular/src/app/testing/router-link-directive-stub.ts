import { Directive, Input } from '@angular/core';

/**
 * Classe stub per la rilevazione di componenti html con RouterLink tra gli attributi
 */
@Directive({
  selector: '[routerLink]',
  host: { '(click)': 'onClick()' }
})
export class RouterLinkDirectiveStub {
  @Input('routerLink') linkParams: any;
  navigatedTo: any = null;

  onClick() {
    this.navigatedTo = this.linkParams;
  }
}