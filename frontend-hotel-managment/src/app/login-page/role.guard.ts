import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router){}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

      //In case user doesn't have required roles, it will move it to login page.
      if(!this.authService.isAuthenticated()){
        this.router.navigate(['/login-page/login']);
        return false;
      }

      let roles = route.data['roles'] as string[];
      let hasRole = false;

      roles.forEach((role, index) => {
        if(this.authService.hasRole(role)) hasRole = true;
      })

      if(hasRole) return true;
      Swal.fire('Acceso denegado', `${this.authService.user.name} no tienes acceso a este recurso!`, 'warning');

    return false;
  }
  
}
