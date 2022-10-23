import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Router } from "@angular/router";
import { catchError, map, Observable, throwError } from "rxjs";
import { URL_BACKEND } from "../config/config";

import { User } from "./user";
import Swal from "sweetalert2";
import { AuthService } from "./auth.service";

@Injectable()
export class UserService{

    private urlEndPoint: string = URL_BACKEND;
    //private urlEndPoint: string = 'http://localhost:8080';
    private httpHeaders = new HttpHeaders({'Content-Type': 'application/json'});

    constructor(private http: HttpClient, private router: Router, private authService: AuthService){}

    /*private authorizationTokenHeaders(){
        let token = this.authService.token;
        if(token != null) return this.httpHeaders.append('Authorization', 'Bearer ' + token);
        return this.httpHeaders;
    }*/

    addUser(user: User): Observable<User>{
        return this.http.post<User>(`${this.urlEndPoint}/api/signup`, user/*, {headers: this.authorizationTokenHeaders()}*/).pipe(
            catchError(e => {

                //Call this method to navigate to login if Status is 401 or 403, so user could log in.
                if(this.authService.isNotAuthorized(e)){
                    return throwError(() => e);
                }

                if(e.status == 400) return throwError(() => e);

                //this.router.navigate(['/login-page/login']);
                Swal.fire('Hubo un error creando al usuario :(', e.error.mensaje, 'error');
                return throwError(() => e);
            })
        )
    }

    recoverPassword(to: string): Observable<User>{

        let formData = new FormData();
        formData.append('to', to);

        return this.http.post<User>(`${this.urlEndPoint}/api/usuarios/recuperar`, formData).pipe(
            catchError(e => {
                
                //When user with that specific email is not found or it occurs an error querying database.
                Swal.fire('Hubo un error al enviar el email :(', e.error.mensaje, 'error');
                return throwError(() => e);
            })
        );

    }

    /*
    private isNotAuthorized(e): boolean{
        //401 is not authorized 
        if(e.status == 401){

            //Once token has expired and we try to get a source, we won't be able to do it because 
            //token isn't valid So ask if user have been authenticated and then just logout to delete 
            //sessionStorage, so user will need to authenticate again.
            if(this.authService.isAuthenticated()){
                this.authService.logout();
            }

            this.router.navigate(['/login-page/login']);
            return true;
        }

        //and 403 forbidden, so user doesn't have needed role.
        if(e.status == 403){
            Swal.fire('Acceso denegado', `${this.authService.user.name}, no tienes acceso a este recurso!`, 'warning');
            return true;
        }
        return false;
    }*/
}