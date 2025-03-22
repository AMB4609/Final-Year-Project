import { Routes } from '@angular/router';
import {Component} from '@angular/core';
import {LoginComponent} from './Components/login/login.component';
import {RegisterComponent} from './Components/register/register.component';
import {DashboardComponent} from './Components/dashboard/dashboard.component';

export const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
    pathMatch: 'full',
  },
  {
    path: 'login',
    redirectTo: '',
    pathMatch: 'full',
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
  }
];
