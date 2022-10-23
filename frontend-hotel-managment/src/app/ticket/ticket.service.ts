import { Injectable } from "@angular/core";
import { Ticket } from "./ticket";

@Injectable()
export class TicketService{
    
    _ticket: Ticket;

    constructor(){}
     
    public get ticket(): Ticket{
        return this._ticket;
    }

    public set ticket(ticket: Ticket){
        this._ticket = ticket;
    }
}