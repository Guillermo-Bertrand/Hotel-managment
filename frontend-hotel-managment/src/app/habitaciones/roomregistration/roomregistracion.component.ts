import { Component, OnInit } from "@angular/core";
import { AuthService } from "src/app/login-page/auth.service";
import Swal from "sweetalert2";
import { BedType } from "../bedtype";
import { Room } from "../room";
import { RoomService } from "../room.service";

@Component({
    selector: 'room-registration',
    templateUrl: './roomregistration.component.html',
    styleUrls: ['./roomregistration.component.css']
})
export class RoomRegistrationComponent implements OnInit{

    newRoom: Room;
    modalActivated: boolean;

    rooms: Room[];

    roomTypes: Room[];
    selectedRoomType: string;
    selectedRoom: Room;

    bedTypes: BedType[];
    selectedBedType: string;

    auxRoom: Room;

    constructor(
      private roomService: RoomService,
      public authService: AuthService){

      this.auxRoom = new Room();

      this.bedTypes = [{idBedType: 1, type: 'Matrimoniales'}, {idBedType: 2, type: 'King size'}];
      this.selectedBedType = 'Matrimoniales';
      
      this.roomTypes = [];
      this.selectedRoomType = 'Habitación estándar';

      this.newRoom = new Room();
    }

    ngOnInit(): void {
        this.loadRooms();
        //And get all room types, which means, get habitacion estandar, habitacion estandar con terraza, etc.
        //to use it in a combo box and have limited options to modify and to create new rooms.
        this.loadRoomTypes();
    }
    
    loadRooms(): void{
      //Get all rooms registered into database.
      this.roomService.showRooms().subscribe(response => {
        
        this.rooms = response as Room[];
      });
    }

    loadRoomData(event): void{

      this.selectedRoom = this.rooms[event.target.value-1];
      this.selectedRoomType = this.selectedRoom.roomType;
      this.selectedBedType = this.selectedRoom.bedType.type;

      this.auxRoom = Object.assign({}, this.selectedRoom);
    }

    loadRoomTypes(): void{
      this.roomService.showRoomTypes().subscribe(response => {

        this.roomTypes = response as Room[];
      })
    }

    addRoom(): void{

      //Set its corresponding bed type.
      if(this.selectedBedType == 'Matrimoniales') this.newRoom.bedType = this.bedTypes[0];
      else this.newRoom.bedType = this.bedTypes[1];

      //And set its corresponding room type.
      this.newRoom.roomType = this.selectedRoomType;
      
      //And finally add it.
      this.roomService.addRoom(this.newRoom).subscribe(response => {

        //After adding new register, add it to array as well to show changes.
        this.rooms.push(response.habitacion);

        Swal.fire('Nueva habitación', 'Habitación añadida éxitosamente!', 'success');
      });
    }

    updateRoom(): void{

      //Set its corresponding bed type.
      if(this.selectedBedType == 'Matrimoniales') this.auxRoom.bedType = this.bedTypes[0];
      else this.auxRoom.bedType = this.bedTypes[1];

      //And set its corresponding room type.
      this.auxRoom.roomType = this.selectedRoomType;
      
      this.roomService.updateRoom(this.auxRoom).subscribe(response => {

        let index = this.rooms.indexOf(this.selectedRoom);
        this.rooms[index] = response.habitacion;

        Swal.fire('Habitación actualizada', `Habitación actualizada éxitosamente!`, 'success');
      });
    }  

    deleteRoom(room: Room): void{
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
                this.roomService.deleteRoom(room.idRoom).subscribe(
                    response => {
                        this.rooms = this.rooms.filter(
                            //Show room's id they're different from the deleted one.
                            auxRoom => auxRoom !== room
                        )
                        Swal.fire('Habitación eliminada', `La habitación ha sido eliminada éxitosamente!`, 'success');
                    }
                )
            }
          })
    }
}