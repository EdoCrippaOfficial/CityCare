import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { PlainLayoutComponent } from './layout/plain-layout/plain-layout.component';
import { ReportComponent } from './report/report.component';
import { ReportDetailComponent } from './report-detail/report-detail.component';
import { AboutComponent } from './about/about.component';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './auth/auth.guard';
import { UserResolver } from './auth/user.resolver';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full'},
  {
    path: '',
    component: PlainLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent}
    ],
    canActivate: [AuthGuard]
  },
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: 'reports', component: ReportComponent, resolve: { data: UserResolver}},
      { path: 'reports/:status/:category', component: ReportComponent, resolve: { data: UserResolver}},
      { path: 'report/:id', component: ReportDetailComponent, resolve: { data: UserResolver}},
      { path: 'about', component: AboutComponent, resolve: { data: UserResolver}},
      { path: 'home', redirectTo: '/reports'}
    ]
  },
  { path: '**', redirectTo: '/login'}
];

/**
 * Modulo per il routing dell'app Angular
 */
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
