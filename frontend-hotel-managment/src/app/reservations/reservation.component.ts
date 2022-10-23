import { Component } from "@angular/core";
import { Router } from "@angular/router";
import Swal from "sweetalert2";
import { BedType } from "../habitaciones/bedtype";
import { Room } from "../habitaciones/room";
import { RoomService } from "../habitaciones/room.service";
import { Ticket } from "../ticket/ticket";
import { TicketService } from "../ticket/ticket.service";
import { Guest } from "./guest";
import { GuestService } from "./guest.service";
import { PaymentMethod } from "./paymentmethod";
import { Reservation } from "./reservation";
import { ReservationService } from "./reservation.service";

@Component({
    selector: 'reservation',
    templateUrl: './reservation.component.html',
    styleUrls: ['./reservation.component.css']
})
export class ReservationComponent{
    
    reservation: Reservation;

    rooms: Room[];
    roomTypes: Room[];
    bedTypes: BedType[];
    paymentMethods: PaymentMethod[];
    guest1: Guest;
    guest2: Guest;
    guest3: Guest;
    guest4: Guest;
    guests: Guest[];
    guestNumbers: number[];

    selectedRoomType: string;
    selectedBedType: string;
    selectedPaymentMethod: string;
    selectedGuestNumber: string;
    total: number;

    price: number;
    hostName: string;
    cardNumber: string;
    expirationDate: string;
    cvv: string;

    checkIn: string;
    checkOut: string;

    constructor(
        private reservationService: ReservationService, 
        private roomService: RoomService,
        private guestService: GuestService,
        private ticketService: TicketService,
        private router: Router){

        this.bedTypes = [{idBedType: 1, type: 'Matrimoniales'}, {idBedType: 2, type: 'King size'}];
        this.paymentMethods = [{idPaymentMethod: 1, payment: 'Efectivo'}, {idPaymentMethod: 2, payment: 'Tarjeta'}, {idPaymentMethod: 3, payment: 'Transferencia'}];
        this.guestNumbers = [1, 2, 3, 4];
        this.selectedRoomType = 'Habitación estándar';
        this.selectedBedType = 'Matrimoniales';
        this.selectedPaymentMethod = 'Efectivo';
        this.selectedGuestNumber = '1';
        this.total = 0;
        this.price = 1000;

        this.roomTypes = [];

        this.checkIn = new Date().toString();
        this.checkOut = new Date().toString();

        this.guest1 = new Guest();
        this.guest2 = new Guest();
        this.guest3 = new Guest();
        this.guest4 = new Guest();

        this.reservation = new Reservation();
    }

    ngOnInit(): void {
        this.loadRooms();
        this.loadRoomTypes();
    }

    loadRoomTypes(): void{
        this.roomService.showRoomTypes().subscribe(response => {
            this.roomTypes = response as Room[];
        });
    }
    
    loadRooms(): void{
        this.roomService.showRooms().subscribe(response => {
            this.rooms = response as Room[];
        });
    }

    AddReservation(): void{

        //Give all properties to this current reservation.

        //Add its room.
        this.rooms.forEach(room => {
            if(room.roomType == this.selectedRoomType && room.bedType.type == this.selectedBedType) this.reservation.room = room;
        });
        //Add dates.
        this.reservation.checkIn = this.checkIn;
        this.reservation.checkOut = this.checkOut;
        //Add total price
        this.reservation.price = this.total;
        //Add payment method.
        this.paymentMethods.forEach(paymentMethod => {
            if(paymentMethod.payment == this.selectedPaymentMethod) this.reservation.paymentMethod = paymentMethod;
        });

        if(this.total == 0){
            Swal.fire('Error al almacenar :(', 'Elija otra fecha de entrada y salida mas coherentes.', 'error');
        }else{
            this.reservationService.addReservation(this.reservation).subscribe({
                next: (response) => {
    
                    let ticket = response.ticket as Ticket;
                    let savedReservation = ticket.reservation as Reservation;
        
                    //Set this ticket to ticket service and the use it in ticket component.
                    this.ticketService.ticket = ticket;
                    
                    //Insert guests into database.
                    for(let i = 0; i < 4; i++){
                        if(parseInt(this.selectedGuestNumber) >= 1 && i == 0) this.addGuest(this.guest1, savedReservation);
                        if(parseInt(this.selectedGuestNumber) >= 2 && i == 1) this.addGuest(this.guest2, savedReservation);
                        if(parseInt(this.selectedGuestNumber) >= 3 && i == 2) this.addGuest(this.guest3, savedReservation);
                        if(parseInt(this.selectedGuestNumber) >= 4 && i == 3) this.addGuest(this.guest4, savedReservation);
                    }
        
                    Swal.fire('Nueva reservación', 'Reservación realizada éxitosamente!', 'success');
                    this.router.navigate(['/ticket']);
                }, error: (e) => {
                    
                }
            });
        }
    }

    addGuest(guest: Guest, reservation: Reservation): void{
        //Add reservation to guest.
        guest.reservation = reservation;
        //And finally add the guest to database.
        this.guestService.addGuest(guest).subscribe(response => {});
    }

    showPrice(){

        //If user change roomtype to frente al mar, then change that string to king size to approach to way binding.
        if(this.selectedRoomType == 'Habitación frente al mar') this.selectedBedType = 'King size';

        this.rooms.forEach(room => {
            if(this.selectedRoomType == room.roomType) this.price = room.price;
        })
        this.calDays();
    }

    changeBedType(){
        if(this.selectedBedType == 'King size') this.selectedGuestNumber = '1';
    }

    calDays(){

        let first = new Date(this.checkIn)
        let last = new Date(this.checkOut);
        let numDays = this.difference(first, last);

        if(numDays > 0){

            this.rooms.forEach(room => {
                if(this.selectedRoomType == room.roomType) this.total = room.price * numDays;
            })
        }else this.total = 0;
    }
    
    private difference(date1, date2) {  
        const date1utc = Date.UTC(date1.getFullYear(), date1.getMonth(), date1.getDate());
        const date2utc = Date.UTC(date2.getFullYear(), date2.getMonth(), date2.getDate());
        let day = 1000*60*60*24;
        return(date2utc - date1utc)/day;
    }
}