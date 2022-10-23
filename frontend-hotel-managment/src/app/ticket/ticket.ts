import { Reservation } from "../reservations/reservation";

export class Ticket{
    idTicket: number;
    generationDate: string;
    reservation: Reservation;
}