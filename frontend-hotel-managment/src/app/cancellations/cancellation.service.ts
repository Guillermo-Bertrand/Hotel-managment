import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import Swal from "sweetalert2";
import { AuthService } from "../login-page/auth.service";
import { ReservationService } from "../reservations/reservation.service";
import { Cancellation } from "./cancellation";

@Injectable()
export class CancellationService{
    
    private urlEndPoint: string = 'http://localhost:8080';

    constructor(private http: HttpClient, private reservationService: ReservationService, private authService: AuthService){}

    showReservation(id: number): Observable<any>{
        return this.http.get<any>(`${this.urlEndPoint}/api/reservaciones/${id}`).pipe(
            catchError(e => {
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);
                
                if(e.status == 400){
                    Swal.fire('Error al buscar la reservación :(', 'Ingrese únicamente digitos numéricos.', 'error');
                    return throwError(() => e);
                }else if(e.status == 404){
                    Swal.fire('Error al buscar la reservación :(', 'No se encontró una reservación con ese id especificado, pruebe a ingresar otro.', 'error');
                    return throwError(() => e);   
                }
                
                Swal.fire('Hubo un error al cargar la reservación :(', e.error.mensaje, 'error');
                
                return throwError(() => e);
            })
        );
    }

    addCancellation(cancellation: Cancellation): Observable<any>{
        return this.http.post<any>(`${this.urlEndPoint}/api/cancelaciones`, cancellation).pipe(
            catchError(e => {

                //Call this method to navigate to login if Status is 401 or 403, so user could log in.
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);

                if(e.status == 400) return throwError(() => e);

                //this.router.navigate(['/login-page/login']);
                Swal.fire('Hubo un error almacenando la cancelación :(', e.error.mensaje, 'error');
                return throwError(() => e);
            })
        );
    }
}