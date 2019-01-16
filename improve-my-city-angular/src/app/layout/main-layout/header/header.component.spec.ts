import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { HeaderComponent } from './header.component';
import { RouterLinkDirectiveStub } from '../../../testing/router-link-directive-stub';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let linkDes;
  let routerLinks;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeaderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // find DebugElements with an attached RouterLinkStubDirective
    linkDes = fixture.debugElement
      .queryAll(By.directive(RouterLinkDirectiveStub));

    // get attached link directive instances
    // using each DebugElement's injector
    routerLinks = linkDes.map(de => de.injector.get(RouterLinkDirectiveStub));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('can get RouterLinks from template', () => {
    expect(routerLinks.length).toBe(15, 'should have 15 routerLinks');
    expect(routerLinks[0].linkParams).toBe('/home');
    expect(routerLinks[1].linkParams).toBe('/home');
    expect(routerLinks[2].linkParams).toBe('/reports');
    expect(routerLinks[3].linkParams).toBe('/reports/0/0');
    expect(routerLinks[4].linkParams).toBe('/reports/1/0');
    expect(routerLinks[5].linkParams).toBe('/reports/2/0');
    expect(routerLinks[6].linkParams).toBe('/reports/3/0');
    expect(routerLinks[7].linkParams).toBe('/reports/4/0');
    expect(routerLinks[8].linkParams).toBe('/reports');
    expect(routerLinks[9].linkParams).toBe('/reports/0/0');
    expect(routerLinks[10].linkParams).toBe('/reports/0/1');
    expect(routerLinks[11].linkParams).toBe('/reports/0/2');
    expect(routerLinks[12].linkParams).toBe('/reports/0/3');
    expect(routerLinks[13].linkParams).toBe('/reports/0/4');
    expect(routerLinks[14].linkParams).toBe('/about');
  });
});
