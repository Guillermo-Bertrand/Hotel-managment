import { Component, OnInit } from '@angular/core';
import { UserService } from '../login-page/user.service';

@Component({
  selector: 'app-password-recovery',
  templateUrl: './password-recovery.component.html'
})
export class PasswordRecoveryComponent implements OnInit {

  to: string;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
  }

  sendMail(): void{
    this.userService.recoverPassword(this.to).subscribe();
  }

}
