import { Routes } from '@angular/router';
import {Component} from '@angular/core';
import {LoginComponent} from './Components/login/login.component';
import {RegisterComponent} from './Components/register/register.component';
import {DashboardComponent} from './Components/dashboard/dashboard.component';
import {ProfileComponent} from './Components/profile/profile.component';
import {HeroComponent} from './Components/hero/hero.component';
import {NotificationComponent} from './Components/notification/notification.component';
import {GetCreditComponent} from './Components/get-credit/get-credit.component';
import {CreditScoreComponent} from './Components/credit-score/credit-score.component';
import { AuthGuard } from './auth.guard';
import {CreditHistoryComponent} from './Components/credit-history/credit-history.component';
import {CreditSummaryComponent} from './Components/credit-summary/credit-summary.component';
import {UserStaffComponent} from './Components/user-staff-component/user-staff-component.component';
import {StaffDashboardComponent} from './Components/staff-dashboard/staff-dashboard.component';
import {ActivityHistoryComponent} from './Components/activity-history/activity-history.component';
import {RoleGuard} from './role.guard';


export const routes: Routes = [
  {
    path: '',
    component: HeroComponent,
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent,
    pathMatch: 'full',
  },
  {
    path: 'register',
    component: RegisterComponent,
    pathMatch: 'full',
  },
  { path: 'profile/:email',
    component: ProfileComponent,
    canActivate: [AuthGuard,RoleGuard],
    data: { roles: ['STAFF', 'ADMIN','USER'] }
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard,RoleGuard],
    data: { roles: ['USER'] }
  },
  {
    path: 'staff-dashboard',
    component: StaffDashboardComponent,
    canActivate: [AuthGuard,RoleGuard],
    data: { roles: ['STAFF', 'ADMIN'] }
  },
  {
    path: 'notification',
    component: NotificationComponent,
    canActivate: [AuthGuard,RoleGuard],
    data: { roles: ['STAFF', 'ADMIN','USER'] }
  },
  {
    path: 'get-credit',
    component: GetCreditComponent,
    canActivate: [AuthGuard,RoleGuard],
    data: { roles: ['USER'] }
  },
  {
    path: 'credit-score',
    component: CreditScoreComponent,
    canActivate: [AuthGuard,RoleGuard],
    data: { roles: ['USER'] }
  },
  {
    path: 'credit-history',
    component: CreditHistoryComponent,
    canActivate: [AuthGuard,RoleGuard],
    data: { roles: ['USER'] }
  },
  {
    path: 'credit-summary',
    component: CreditSummaryComponent,
    canActivate: [AuthGuard,RoleGuard],
    data: { roles: ['USER'] }
  },
  {
    path: 'User-Staff',
    component: UserStaffComponent,
    canActivate: [AuthGuard,RoleGuard],
    data: { roles: ['STAFF', 'ADMIN'] }
  },
  {
    path: 'activity-history/:userEmail',
    component: ActivityHistoryComponent,
    canActivate: [AuthGuard,RoleGuard],
    data: { roles: ['STAFF', 'ADMIN'] }
  }
];


