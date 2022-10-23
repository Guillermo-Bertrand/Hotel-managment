import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { catchError, Observable, throwError } from "rxjs";
import Swal from "sweetalert2";
import { AuthService } from "../login-page/auth.service";
import { Room } from "./room";
import { URL_BACKEND } from "../config/config";

@Injectable()
export class RoomService{
    
    private urlEndPoint: string = URL_BACKEND;
    //private httpHeaders = new HttpHeaders({'Content-Type': 'application/json'});

    constructor(private http: HttpClient, private authService: AuthService, private router: Router){}

    showRoomTypes(): Observable<Room[]>{
        return this.http.get<Room[]>(`${this.urlEndPoint}/api/habitaciones`);
    }

    showRooms(): Observable<Room[]>{
        return this.http.get<Room[]>(`${this.urlEndPoint}/api/registro-habitaciones`).pipe(
            catchError(e => {
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);
                return throwError(() => e);
            })
        );
    }

    showRoom(id: number): Observable<Room>{
        return this.http.get<Room>(`${this.urlEndPoint}/api/registro-habitaciones/${id}`).pipe(
            catchError(e => {
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);
                
                if(e.status == 400) return throwError(() => e);
                
                Swal.fire('Hubo un error al cargar la habitación :(', e.error.mensaje, 'error');
                
                return throwError(() => e);
            })
        );
    }

    addRoom(room: Room): Observable<any>{
        return this.http.post<any>(`${this.urlEndPoint}/api/registro-habitaciones`, room).pipe(
            catchError(e => {

                //Call this method to navigate to login if Status is 401 or 403, so user could log in.
                if(this.authService.isNotAuthorized(e)) return throwError(() => e);

                if(e.status == 400){
                    Swal.fire('Error al almacenar :(', 'Verifique sus entradas en los campos disponibles', 'error');
                    return throwError(() => e);
                }

                Swal.fire('Hubo un error almacenando la habitación :(', e.error.mensaje, 'error');
                return throwError(() => e);
            })
        );
    }

    updateRoom(room: Room): Observable<any>{
        return this.http.put<any>(`${this.urlEndPoint}/api/registro-habitaciones/${room.idRoom}`, room).pipe(
            catchError(e => {

                if(this.authService.isNotAuthorized(e)) return throwError(() => e);

                if(e.status == 400){
                    Swal.fire('Error al actualizar :(', 'Verifique sus entradas en los campos disponibles', 'error');
                    return throwError(() => e);
                }

                Swal.fire('Hubo un error actualizando la habitación :(', e.error.mensaje, 'error');
                return throwError(() => e);
            })
        );
    }

    deleteRoom(id: number): Observable<Room>{
        return this.http.delete<Room>(`${this.urlEndPoint}/api/registro-habitaciones/${id}`).pipe(
            catchError(e => {
                Swal.fire('Hubo un error al eliminar la habitación :(', e.error.mensaje, 'error');
                return throwError(() => e);
            })
        );
    }
}