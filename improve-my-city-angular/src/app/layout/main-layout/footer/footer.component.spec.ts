import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';

import { FooterComponent } from './footer.component';
import { RouterLinkDirectiveStub } from '../../../testing/router-link-directive-stub';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;
  let linkDes;
  let routerLinks;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FooterComponent ]
    })
    .compileComponents();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect properly', () => {
    let href = fixture.debugElement.query(By.css('a')).nativeElement
              .getAttribute('href');
    expect(href).toEqual('/settings/testing/edit/1');
  })
});
