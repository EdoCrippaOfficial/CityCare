import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
//Routing
import { AppRoutingModule } from './app-routing.module';
//Default component
import { AppComponent } from './app.component';
//UI
import { UiModule } from './ui/ui.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
//Components
import { ReportComponent } from './report/report.component';
//Firebase
import { AngularFireModule } from '@angular/fire';
import { AngularFirestoreModule } from '@angular/fire/firestore';
import { AngularFireStorageModule } from '@angular/fire/storage';
import { AngularFireAuthModule } from '@angular/fire/auth';
import { environment } from '../environments/environment';

@NgModule({
  declarations: [
    AppComponent,
    ReportComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    UiModule,
    NgbModule, //bootstrap stuff
    AngularFireModule.initializeApp(environment.firebase, 'improvemycity'), // imports firebase/app needed for everything
    AngularFirestoreModule, // imports firebase/database, only needed for database features
    AngularFireAuthModule, // imports firebase/auth, only needed for auth features,
    AngularFireStorageModule // imports firebase/storage only needed for storage features
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
