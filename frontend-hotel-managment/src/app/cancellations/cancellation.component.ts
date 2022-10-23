import { Component, OnInit } from "@angular/core";
import Swal from "sweetalert2";
import { Reservation } from "../reservations/reservation";
import { Cancellation } from "./cancellation";
import { CancellationService } from "./cancellation.service";

@Component({
    selector: 'cancellation',
    templateUrl: './cancellation.component.html',
    styleUrls: ['./cancellation.component.css']
})
export class CancellationComponent{
    
    id: number;
    idAux: number;
    refund: number;
    now: string;
    cancellation: Cancellation;
    reservation: Reservation;

    reservationLooked: boolean;

    constructor(private cancellationService: CancellationService){
        this.now = Date.now().toString();
        this.cancellation = new Cancellation();
        this.reservationLooked = false;
    }

    loadReservation(): void{
        this.idAux = this.id;
        this.cancellationService.showReservation(this.id).subscribe({
            next: (response) => {
            
                this.reservation = response as Reservation;
    
                //calculate refund.
                let cancellationDate = new Date();
                let checkInDate = new Date(this.reservation.checkIn);
                let checkOutDate = new Date(this.reservation.checkOut);
    
                //If reservation hasn't started, then refund all money.
                if(cancellationDate <= checkInDate){
    
                    this.refund = this.reservation.price;
    
                }else if(cancellationDate >= checkOutDate){
                    this.refund = 0;
                }else if(cancellationDate >= checkInDate){//Otherwise calculate how much money to refund.
    
                    let numDays = this.difference(cancellationDate, checkOutDate);
                    //Calculate how much is the refund to show it in table.
                    this.refund = this.reservation.room.price * numDays;
                }
    
                //Finally set this data to cancellation's properties.
                this.cancellation.date = cancellationDate.toJSON();
                this.cancellation.reservation = this.reservation;
                this.cancellation.refund = this.refund;
    
                this.reservationLooked = true;
            }
        });
    }

    addCancellation(): void{

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

                if(this.refund == 0) Swal.fire("Error al realizar cancelación :(", "No es posible cancelar esa reservación ya que ya ha terminado.", "error");
                else{
                    this.cancellationService.addCancellation(this.cancellation).subscribe(response => {
                        Swal.fire("Nueva cancelación", "La cancelación se ha realizado éxitosamente", "success");
                    });
                }

            }
          })
    }

    private difference(date1, date2) {  
        const date1utc = Date.UTC(date1.getFullYear(), date1.getMonth(), date1.getDate());
        const date2utc = Date.UTC(date2.getFullYear(), date2.getMonth(), date2.getDate());
        let day = 1000*60*60*24;
        return(date2utc - date1utc)/day;
     }
}