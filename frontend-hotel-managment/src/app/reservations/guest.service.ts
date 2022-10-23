import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, throwError } from "rxjs";
import Swal from "sweetalert2";
import { AuthService } from "../login-page/auth.service";
import { Guest } from "./guest";
import { URL_BACKEND } from "../config/config";

@Injectable()
export class GuestService{

    private urlEndPoint: string = URL_BACKEND;

    constructor(private http: HttpClient, private authService: AuthService){}

    addGuest(guest: Guest): Observable<Guest>{
        return this.http.post<Guest>(`${this.urlEndPoint}/api/huespedes`, guest).pipe(
            catchError(e => {
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);
                
                if(e.status == 400) return throwError(() => e);
                
                Swal.fire('Hubo un error al almacenar al huésped :(', e.error.mensaje, 'error');
                
                return throwError(() => e);
            })
        );
    }

    updateGuest(guest: Guest): Observable<Guest>{
        return this.http.put<Guest>(`${this.urlEndPoint}/api/huespedes/${guest.idGuest}`, guest).pipe(
            catchError(e => {
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);
                
                if(e.status == 400) return throwError(() => e);
                
                Swal.fire('Hubo un error al actualizar al huésped :(', e.error.mensaje, 'error');
                
                return throwError(() => e);
            })
        );
    }
}