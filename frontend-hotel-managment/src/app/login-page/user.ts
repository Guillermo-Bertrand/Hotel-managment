import { Role } from "./role";
import { SocialNetwork } from "./signup/socialnetwork";

export class User{
    idUser: number;
    name: string;
    lastName: string;
    creationDate: string;
    address: string;
    socialNetworks: SocialNetwork[];

    email: string;
    password: string;
    roles: Role[];
    enabled: boolean;
}