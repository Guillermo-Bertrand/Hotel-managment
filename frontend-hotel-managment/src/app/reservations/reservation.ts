import { Room } from "../habitaciones/room";
import { PaymentMethod } from "./paymentmethod";

export class Reservation{
    idReservation: number;
    price: number;
    checkIn: string;
    checkOut: string;
    officialDocument: string;
    paymentMethod: PaymentMethod;
    room: Room;
    enabled: boolean;
}