import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
//Routing
import { AppRoutingModule } from './app-routing.module';
//Default component
import { AppComponent } from './app.component';
//UI
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
//Forms
import { FormsModule } from '@angular/forms';
//Components
import { ReportComponent } from './report/report.component';
//Firebase
import { AngularFireModule } from '@angular/fire';
import { AngularFirestoreModule } from '@angular/fire/firestore';
import { AngularFireStorageModule } from '@angular/fire/storage';
import { AngularFireAuthModule } from '@angular/fire/auth';
import { environment } from '../environments/environment';
import { ReportDetailComponent } from './report-detail/report-detail.component';
import { AboutComponent } from './about/about.component';
import { LoginComponent } from './login/login.component';
import { PlainLayoutComponent } from './layout/plain-layout/plain-layout.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { HeaderComponent } from './layout/main-layout/header/header.component';
import { FooterComponent } from './layout/main-layout/footer/footer.component';

@NgModule({
  declarations: [
    AppComponent,
    ReportComponent,
    ReportDetailComponent,
    AboutComponent,
    LoginComponent,
    PlainLayoutComponent,
    MainLayoutComponent,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule.forRoot(), //bootstrap stuff
    FormsModule, //Forms stuff
    AngularFireModule.initializeApp(environment.firebase, 'improvemycity'), // imports firebase/app needed for everything
    AngularFirestoreModule, // imports firebase/database, only needed for database features
    AngularFireAuthModule, // imports firebase/auth, only needed for auth features,
    AngularFireStorageModule // imports firebase/storage only needed for storage features
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
