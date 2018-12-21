import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ReportComponent } from './report/report.component';
import { ReportDetailComponent } from './report-detail/report-detail.component';
import { AboutComponent } from './about/about.component';

const routes: Routes = [
  { path: 'reports', component: ReportComponent},
  { path: 'reports/:status', component: ReportComponent},
  { path: 'report/:id', component: ReportDetailComponent },
  { path: 'about', component: AboutComponent},
  { path: 'home', redirectTo: '/reports', pathMatch: 'full' },
  { path: '', redirectTo: '/home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
