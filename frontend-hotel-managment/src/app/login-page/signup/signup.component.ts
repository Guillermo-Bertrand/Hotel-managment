import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { User } from '../user';
import { UserService } from '../user.service';
import { SocialNetwork } from './socialnetwork';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html'
})
export class SignupComponent implements OnInit {

  user: User;
  socialNetworks: SocialNetwork[];
  selectedSocialNetworks: SocialNetwork[];

  constructor(private userService: UserService, private router: Router) { 
    this.socialNetworks = [
      {idSocialNetwork: 1, socialNetwork: 'Facebook'},
      {idSocialNetwork: 2, socialNetwork: 'Instagram'},
      {idSocialNetwork: 3, socialNetwork: 'Twitter'},
      {idSocialNetwork: 4, socialNetwork: 'Tiktok'},
    ]
    this.selectedSocialNetworks = [];
    this.user = new User();
  }

  ngOnInit(): void {
  }

  //This method will add just the checked social networks.
  stateChanged(checkBox: any){
    if(checkBox.checked){
      
      let selectedSN;

      this.socialNetworks.forEach(sn => {
        if(sn.socialNetwork == checkBox.value) selectedSN = sn;
      });

      this.selectedSocialNetworks.push(selectedSN);
    }else{

      let auxSelSolNet: SocialNetwork[] = [];

      this.selectedSocialNetworks.forEach(socialNetwork => {
        if(socialNetwork.socialNetwork != checkBox.value) auxSelSolNet.push(socialNetwork);
      });

      this.selectedSocialNetworks = auxSelSolNet;
    }
  }

  addUser(): void{

    //Put user's social networks.
    this.user.socialNetworks = this.selectedSocialNetworks;
    //Put its single role
    this.user.roles = [{idRole: 1, role: 'ROLE_USER'}];
    //An finally put its now date.
    this.user.creationDate = Date.now().toString();

    this.userService.addUser(this.user).subscribe(response => {
      Swal.fire('Creación de usuario', 'Se ha creado el usuario éxitosamente!', 'success');
    });
  }
}
