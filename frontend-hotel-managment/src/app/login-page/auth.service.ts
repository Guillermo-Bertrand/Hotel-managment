import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { catchError, Observable, throwError } from "rxjs";
import Swal from "sweetalert2";
import { Role } from "./role";
import { User } from "./user";
import { URL_BACKEND } from "../config/config";

@Injectable()
export class AuthService{

    private _user: User;
    private _token: string;

    //private urlEndPoint: string = 'https://spring-boot-hotel-managment.herokuapp.com/oauth/token';
    private urlEndPoint: string = URL_BACKEND;
    //Btoa function works to encrypt that given information.
    private credentials: string = btoa('angularapp' + ':' + '12345');
    private params = new URLSearchParams();

    private httpHeaders = new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded', 
        'Authorization': 'Basic ' + this.credentials
    });

    constructor(private http: HttpClient, private router: Router){}

    //This method will do authentication.
    login(user: User): Observable<any>{

        this.params.set('username', user.email);
        this.params.set('password', user.password);
        this.params.set('grant_type', 'password');

        return this.http.post<any>(`${this.urlEndPoint}/oauth/token`, this.params.toString(), {headers: this.httpHeaders}).pipe(
            catchError(e => {
                if(e.status == 400) Swal.fire('Error al iniciar sesión', 'Usuario o clave incorrectas!', 'error');
                return throwError(() => e);
            })
        );
    }

    logout(): void{
        //To logout put those variables in null and clear sessionstorage.
        this._token = null;
        this._user = null;
        sessionStorage.clear();
        //Or in case of having more information in sessionStorage, just delete specific items from there.
        /*
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('user');
        */
    }

    hasRole(role: string): boolean{
        let userRoles = this.user.roles as Array<Role>;
        let roleIncluded = false;

        userRoles.forEach(userRole => {
            if(userRole.toString() == role) roleIncluded = true;
        });

        if(userRoles.length > 0 && roleIncluded) return true;
        return false;
    }

    saveUser(accessToken: string): void{

        let payload = this.obtainPayload(accessToken);

        this._user = new User();
        this._user.name = payload.name;
        this._user.lastName = payload.last_name;
        this._user.email = payload.email;
        this._user.roles = payload.authorities;

        //JSON.stringify converts an object into a string.
        sessionStorage.setItem('user', JSON.stringify(this._user));
    }

    saveToken(accessToken: string): void{

        this._token = accessToken;
        sessionStorage.setItem('token', accessToken);
    }

    obtainPayload(accessToken: string): any{
        if(accessToken != null) return JSON.parse(atob(accessToken.split(".")[1]));
        return null;
    }

    isAuthenticated(): boolean{
        //Get token by method token, so write it underscoreless.
        let payload = this.obtainPayload(this.token);
        //If that method returns a not null payload and has an email property 
        //which also has a length greater than 0, user is authenticated.
        if(payload != null && payload.email && payload.email.length > 0) return true;
        return false;
    }

    public isNotAuthorized(e): boolean{
        //401 is not authorized 
        if(e.status == 401){

            //Once token has expired and we try to get a source, we won't be able to do it because 
            //token isn't valid So ask if user have been authenticated and then just logout to delete 
            //sessionStorage, so user will need to authenticate again.
            if(this.isAuthenticated()){
                this.logout();
            }

            this.router.navigate(['/login-page/login']);
            return true;
        }

        //and 403 forbidden, so user doesn't have needed role.
        if(e.status == 403){
            Swal.fire('Acceso denegado', `${this.user.name}, no tienes acceso a este recurso!`, 'warning');
            return true;
        }
        return false;
    }

    public get user(): User{
        //If login process just happened, it won't be null.
        if(this._user != null) return this._user;
        //But if for example, user close the navigator like chrome, it will use session storage.
        else if(this._user == null && sessionStorage.getItem('user') != null) return JSON.parse(sessionStorage.getItem('user')) as User;
        //In another case were user doesn't exist, which means, it has never logged in, just return a new empty user.
        else{
            let user = new User();
            user.roles = [];
            return user;
        }
    }

    public get token(): string{
        //If login process just happened, it won't be null.
        if(this._token != null) return this._token;
        //But if for example, user close the navigator like chrome, it will use session storage.
        else if(this._token == null && sessionStorage.getItem('token') != null) return sessionStorage.getItem('token');
        return null;
    }
}