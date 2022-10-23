import { Component, OnInit } from '@angular/core';
import { Reservation } from '../reservations/reservation';
import { Ticket } from './ticket';
import { TicketService } from './ticket.service';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.css']
})
export class TicketComponent implements OnInit {

  ticket: Ticket;

  constructor(private ticketService: TicketService) { 
  }

  ngOnInit(): void {
    this.ticket = this.ticketService.ticket;
  }



}