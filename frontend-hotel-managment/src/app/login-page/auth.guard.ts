import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router){}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      
      //This guard works to know whether user is authenticatedor not, so if it needs to authenticate it will move to login.

      if(this.authService.isAuthenticated()){

        //If token has expired, logout to clear sessionStorage and move user to login page.
        if(this.tokenHasExpired()){
          this.authService.logout();
          Swal.fire('Autenticación', 'Tu sesión ha expirado, por favor vuelva a iniciar sesión!', 'info');
          this.router.navigate(['/login-page/login']);
          return false;
        }

        return true;
      }
      this.router.navigate(['/login-page/login']);
    return false;
  }
  

  tokenHasExpired(): boolean{
    let token = this.authService.token;
    let payload = this.authService.obtainPayload(token);
    let now = new Date().getTime()/1000;

    //If token's expiration date is lesser than now's date, token has expired.
    if(payload.exp < now) return true;
    return false;
  }
}
