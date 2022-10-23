import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { LoginComponent } from './login-page/login/login.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { SignupComponent } from './login-page/signup/signup.component';
import { HabitacionesComponent } from './habitaciones/habitaciones.component';
import { MainPageComponent } from './main-page/main-page.component';
import { PasswordRecoveryComponent } from './password-recovery/password-recovery.component';
import { MailPostComponent } from './mail-post/mail-post.component';
import { RoomRegistrationComponent } from './habitaciones/roomregistration/roomregistracion.component';
import { TicketComponent } from './ticket/ticket.component';
import { CancellationComponent } from './cancellations/cancellation.component';
import { ReservationComponent } from './reservations/reservation.component';
import { ReservationRegisterComponent } from './reservations/reservationsregister/reservationregister.component';

import { UserService } from './login-page/user.service';
import { RoomService } from './habitaciones/room.service';
import { ReservationService } from './reservations/reservation.service';
import { GuestService } from './reservations/guest.service';
import { TicketService } from './ticket/ticket.service';
import { CancellationService } from './cancellations/cancellation.service';
import { AuthService } from './login-page/auth.service';
import { AuthGuard } from './login-page/auth.guard';
import { RoleGuard } from './login-page/role.guard';
import { TokenInterceptor } from './login-page/token.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    LoginComponent,
    LoginPageComponent,
    SignupComponent,
    HabitacionesComponent,
    MainPageComponent,
    PasswordRecoveryComponent,
    MailPostComponent,
    RoomRegistrationComponent, 
    TicketComponent,
    CancellationComponent,
    ReservationComponent,
    ReservationRegisterComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([
      {path: 'login-page', component: LoginPageComponent, children: [
        {path: 'signup', component: SignupComponent, canActivate: [AuthGuard, RoleGuard], data: {roles: ['ROLE_ADMIN']} },
        {path: 'login', component: LoginComponent}
      ]},
      {path: 'habitaciones', component: HabitacionesComponent },
      {path: 'registro-habitaciones', component: RoomRegistrationComponent, canActivate: [AuthGuard, RoleGuard], data: {roles: ['ROLE_ADMIN', 'ROLE_USER']}},
      {path: 'cancelaciones', component: CancellationComponent, canActivate: [AuthGuard, RoleGuard], data: {roles: ['ROLE_ADMIN']} },
      {path: 'reservaciones', component: ReservationComponent, canActivate: [AuthGuard, RoleGuard], data: {roles: ['ROLE_ADMIN', 'ROLE_USER']}},
      {path: 'registro-reservaciones', component: ReservationRegisterComponent, canActivate: [AuthGuard, RoleGuard], data: {roles: ['ROLE_ADMIN', 'ROLE_USER']}},
      {path: 'ticket', component: TicketComponent, canActivate: [AuthGuard, RoleGuard], data: {roles: ['ROLE_ADMIN', 'ROLE_USER']}},
      {path: 'main-page', component: MainPageComponent },
      {path: 'password-recovery', component: PasswordRecoveryComponent},
      {path: 'mail-post', component: MailPostComponent },
      {path: '', redirectTo: '/main-page', pathMatch: 'full'},
      {path: '**', component: PageNotFoundComponent}
    ]),
    FormsModule,
    HttpClientModule
  ],
  providers: [
    UserService,
    AuthService,
    RoomService,
    ReservationService,
    GuestService,
    TicketService,
    CancellationService,
    {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
