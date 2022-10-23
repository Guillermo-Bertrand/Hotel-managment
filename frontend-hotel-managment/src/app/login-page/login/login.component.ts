import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../auth.service';
import { User } from '../user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  user: User = new User();

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    //This way user won't be able to see login component again, at least while it is logged in.
    if(this.authService.isAuthenticated()){
      this.router.navigate(['/habitaciones']);
      Swal.fire('Inicio de sesión', `Hola ${this.authService.user.name}, ya estás autenticado!`, 'info');
    }
  }

  login(): void{

    //If any of these fields are empty, show error message to user.
    if(this.user.email == null || this.user.password == null){

      let empty: string;

      if(this.user.email == null && this.user.password == null) empty = 'Favor de introducir email y contraseña.'
      else if(this.user.email == null) empty = 'Favor de introducir el email.'
      else if(this.user.password == null) empty = 'Favor de introducir la contraseña.'
      
      return;
    }

    this.authService.login(this.user).subscribe({
      next: (response) => {
        this.authService.saveUser(response.access_token);
        this.authService.saveToken(response.access_token);

        let user = this.authService.user;

        this.router.navigate(['/habitaciones']);
        Swal.fire('Inicio de sesión', `Hola ${user.name}, haz iniciado sesión con éxito!`, 'success');
      },
      error: (e) => {
        Swal.fire('Error al iniciar sesión', 'Usuario o clave incorrectas!', 'error');
      }
    });
  }

}
