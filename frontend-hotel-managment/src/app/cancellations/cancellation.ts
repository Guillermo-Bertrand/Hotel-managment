import { Reservation } from "../reservations/reservation";

export class Cancellation{
    idCancellation: number;
    date: string;
    refund: number;
    reservation: Reservation;
}