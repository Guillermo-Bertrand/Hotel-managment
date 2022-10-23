import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { BedType } from "src/app/habitaciones/bedtype";
import { Room } from "src/app/habitaciones/room";
import { RoomService } from "src/app/habitaciones/room.service";
import { AuthService } from "src/app/login-page/auth.service";
import { Ticket } from "src/app/ticket/ticket";
import { TicketService } from "src/app/ticket/ticket.service";
import Swal from "sweetalert2";
import { Guest } from "../guest";
import { GuestService } from "../guest.service";
import { Reservation } from "../reservation";
import { ReservationService } from "../reservation.service";

@Component({
    selector: 'reservation-register',
    templateUrl: './reservationregister.component.html',
    styleUrls: ['./reservationregister.component.css']
})
export class ReservationRegisterComponent implements OnInit{

    reservations: Reservation[];

    auxReservation: Reservation;
    selectedReservation: Reservation;

    selectedRoomType: string;
    roomTypes: Room[];

    rooms: Room[];
    
    selectedBedType: string;
    bedTypes: BedType[];

    checkIn: string;
    checkOut: string;

    guest1: Guest;
    guest2: Guest;
    guest3: Guest;
    guest4: Guest;
    selectedGuestNumber: string;

    price: number;
    total: number;

    constructor(
        private reservationService: ReservationService,
        private roomService: RoomService,
        private guestService: GuestService,
        private ticketService: TicketService,
        public authService: AuthService,
        private router: Router){

        this.reservations = [];
        this.auxReservation = new Reservation();

        this.bedTypes = [{idBedType: 1, type: 'Matrimoniales'}, {idBedType: 2, type: 'King size'}];

        this.roomTypes = [];
        this.rooms = [];

        this.checkIn = new Date().toString();
        this.checkOut = new Date().toString();

        this.guest1 = new Guest();
        this.guest2 = new Guest();
        this.guest3 = new Guest();
        this.guest4 = new Guest();
        this.selectedGuestNumber = '1';

        this.total = 0;
    }

    ngOnInit(): void {
        this.loadReservations();
        this.loadRoomTypes();
        this.loadRooms();
    }

    loadTicket(id: number): void{

        this.reservationService.showTicket(id).subscribe(response => {
            this.ticketService.ticket = response as Ticket;

            this.router.navigate(['/ticket']);
        });
    }

    loadReservations(): void{
        this.reservationService.showReservations().subscribe(response => {
            this.reservations = response as Reservation[];
            //Filter items by its state, if any oh them are not enabled, not to take them in account.
            this.reservations = this.reservations.filter(reservation => reservation.enabled)
        });
    }

    loadReservationData(event){
        this.selectedReservation = this.reservations.find(reservation => reservation.idReservation == event.target.value);
        this.selectedRoomType = this.selectedReservation.room.roomType;
        this.selectedBedType = this.selectedReservation.room.bedType.type;
        this.checkIn = this.selectedReservation.checkIn;
        this.checkOut = this.selectedReservation.checkOut;
        this.price = this.selectedReservation.room.price;
        this.total = this.selectedReservation.price;

        //Get guests related to this reservation.
        this.reservationService.showGuests(this.selectedReservation.idReservation).subscribe(response => {
            
            //Set up guest number and guests objects.
            this.selectedGuestNumber = response.length.toString();

            if(response.length >= 1) this.guest1 = response[0] as Guest;
            if(response.length >= 2) this.guest2 = response[1] as Guest;
            if(response.length >= 3) this.guest3 = response[2] as Guest;
            if(response.length >= 4) this.guest4 = response[3] as Guest;
        })

        this.auxReservation = Object.assign({}, this.selectedReservation);
        
    }

    loadRooms(){
        this.roomService.showRooms().subscribe(response => {
            this.rooms = response as Room[];
        });
    }

    loadRoomTypes(){
        this.roomService.showRoomTypes().subscribe(response => {
            this.roomTypes = response as Room[];
        });
    }

    showPrice(){
        this.roomTypes.forEach(room => {
            if(this.selectedRoomType == room.roomType) this.price = room.price;
        })
        this.calDays();
    }

    calDays(){

        let first = new Date(this.checkIn)
        let last = new Date(this.checkOut);
        let numDays = this.difference(first, last);

        if(numDays > 0){

            this.roomTypes.forEach(room => {
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

    updateReservation(){

        //Set changed and possible changed properties.
        this.rooms.forEach(room => {
            if(room.roomType == this.selectedRoomType && room.bedType.type == this.selectedBedType) this.auxReservation.room = room;
        });

        this.auxReservation.checkIn = this.checkIn;
        this.auxReservation.checkOut = this.checkOut;
        this.auxReservation.price = this.total;

        if(this.total == 0){
            Swal.fire('Error al actualizar :(', 'Elija otra fecha de entrada y salida mas coherentes.', 'error');
        }else{
            this.reservationService.updateReservation(this.auxReservation).subscribe({
                next: (response) => {
    
                    //Once reservation has been updated, update guests as well.
                    for(let i = 0; i < 4; i++){
                        if(parseInt(this.selectedGuestNumber) >= 1 && i == 0) this.updateGuest(this.guest1);
                        if(parseInt(this.selectedGuestNumber) >= 2 && i == 1) this.updateGuest(this.guest2);
                        if(parseInt(this.selectedGuestNumber) >= 3 && i == 2) this.updateGuest(this.guest3);
                        if(parseInt(this.selectedGuestNumber) >= 4 && i == 3) this.updateGuest(this.guest4);
                    }
    
                    //And update register into array dynamically
                    let index = this.reservations.indexOf(this.selectedReservation);
                    this.reservations[index] = this.auxReservation;
    
                    Swal.fire('Reservación', 'Reservación actualizada éxitosamente!', 'success');
                },
                error: (e) => {
                    Swal.fire('Error al almacenar :(', 'Verifique sus entradas en los campos disponibles', 'error');
                }
            });
        }
    }

    updateGuest(guest: Guest){
        this.guestService.updateGuest(guest).subscribe();
    }

    deleteReservation(reservation: Reservation){
        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
              confirmButton: 'btn btn-success',
              cancelButton: 'btn btn-danger'
            },
            buttonsStyling: false
          })
          
          swalWithBootstrapButtons.fire({
            title: 'Estás seguro?',
            text: `No podrás revertir este proceso!`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Si, borrar!',
            cancelButtonText: 'No, cancelar!',
            reverseButtons: true
          }).then((result) => {

            if (result.isConfirmed) {
                this.reservationService.deleteReservation(reservation.idReservation).subscribe(
                    response => {
                        this.reservations = this.reservations.filter(
                            //Show reservation's id they're different from the deleted one.
                            auxRes => auxRes !== reservation
                        )
                        Swal.fire('Reservación eliminada', `La reservación ha sido eliminada éxitosamente!`, 'success');
                    }
                )
            }
          })
    }
}