import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { PlainLayoutComponent } from './layout/plain-layout/plain-layout.component';
import { ReportComponent } from './report/report.component';
import { ReportDetailComponent } from './report-detail/report-detail.component';
import { AboutComponent } from './about/about.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full'},
  {
    path: '',
    component: PlainLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent}
    ]
  },
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: 'reports', component: ReportComponent},
      { path: 'reports/:status', component: ReportComponent},
      { path: 'report/:id', component: ReportDetailComponent },
      { path: 'about', component: AboutComponent},
      { path: 'home', redirectTo: '/reports'}
    ]
  },
  { path: '**', redirectTo: '/login'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
