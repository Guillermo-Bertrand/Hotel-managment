import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import Swal from "sweetalert2";
import { AuthService } from "../login-page/auth.service";
import { Reservation } from "./reservation";
import { URL_BACKEND } from "../config/config";
import { Guest } from "./guest";

@Injectable()
export class ReservationService{
    
    private urlEndPoint: string = URL_BACKEND; 

    constructor(private http: HttpClient, private authService: AuthService){}

    addReservation(reservation: Reservation): Observable<any>{
        return this.http.post<any>(`${this.urlEndPoint}/api/reservaciones`, reservation).pipe(
            catchError(e => {

                //Call this method to navigate to login if Status is 401 or 403, so user could log in.
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);

                if(e.status == 400) return throwError(() => e);

                Swal.fire('Hubo un error almacenando la reservación :(', e.error.mensaje, 'error');
                return throwError(() => e);
            })
        );
    }

    updateReservation(reservation: Reservation): Observable<any>{
        return this.http.put<any>(`${this.urlEndPoint}/api/reservaciones/${reservation.idReservation}`, reservation).pipe(
            catchError(e => {

                if(this.authService.isNotAuthorized(e)) return throwError(() => e);

                if(e.status == 400) return throwError(() => e);

                Swal.fire('Hubo un error actualizando la reservación :(', e.error.mensaje, 'error');
                return throwError(() => e);
            })
        )
    }

    deleteReservation(id: number): Observable<Reservation>{
        return this.http.delete<Reservation>(`${this.urlEndPoint}/api/reservaciones/${id}`).pipe(
            catchError(e => {
                Swal.fire('Hubo un error al eliminar la reservación :(', e.error.mensaje, 'error');
                return throwError(() => e);
            })
        )
    }

    showReservations(): Observable<Reservation[]>{
        return this.http.get<Reservation[]>(`${this.urlEndPoint}/api/reservaciones`).pipe(
            catchError(e => {
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);
                return throwError(() => e);
            })
        );
    }

    showTicket(id: number): Observable<any>{
        return this.http.get<any>(`${this.urlEndPoint}/api/reservaciones-tickets/${id}`).pipe(
            catchError(e => {
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);
                return throwError(() => e);
            })
        );
    }

    showGuests(id: number): Observable<Guest[]>{
        return this.http.get<Guest[]>(`${this.urlEndPoint}/api/reservaciones-huespedes/${id}`).pipe(
            catchError(e => {
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);
                return throwError(() => e);
            })
        );
    }
}