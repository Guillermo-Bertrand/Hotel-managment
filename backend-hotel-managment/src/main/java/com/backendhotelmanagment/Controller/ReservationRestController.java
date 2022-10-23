package com.backendhotelmanagment.Controller;

import com.backendhotelmanagment.Entity.Guest;
import com.backendhotelmanagment.Entity.Reservation;
import com.backendhotelmanagment.Entity.Ticket;
import com.backendhotelmanagment.Service.ReservationService;
import com.backendhotelmanagment.Service.TicketService;
import com.backendhotelmanagment.Service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin({"http://localhost:4200", "https://angular-hotel-managment.web.app"})
public class ReservationRestController {

    private final ReservationService reservationService;
    private final TicketService ticketService;
    private final UploadService uploadService;

    @Autowired
    public ReservationRestController(ReservationService reservationService, TicketService ticketService, UploadService uploadService){
        this.reservationService = reservationService;
        this.ticketService = ticketService;
        this.uploadService = uploadService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/reservaciones-tickets/{id}")
    public Ticket showTicket(@PathVariable Long id){
        return ticketService.findById(id);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/reservaciones-huespedes/{id}")
    public List<Guest> showRelatedGuests(@PathVariable Long id){
        return reservationService.findGuest(id);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/reservaciones")
    public List<Reservation> showReservations(){
        return reservationService.findAll();
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/reservaciones/{id}")
    public ResponseEntity<?> showReservation(@PathVariable Long id){

        Reservation reservation;
        Map<String, Object> response = new HashMap<>();

        try{
            reservation = reservationService.findById(id);

            if(reservation == null){
                response.put("mensaje", "Reservacion id: " + id + " no existe en la base de datos!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/reservaciones")
    public ResponseEntity<?> addReservation(@Valid @RequestBody Reservation reservation, BindingResult result){

        Reservation returnedReservation;
        Ticket ticket;
        Map<String, Object> response = new HashMap<>();

        //If there are any errors coming from request, gather and return them in responseEntity.
        if(result.hasErrors()){
            //Map fieldErrors to transform every fieldError in string, it's similar to javaScript.
            List<String> errors = result.getFieldErrors().stream().map(
                    error -> "Campo " + error.getField() + ": " + error.getDefaultMessage()
            ).collect(Collectors.toList());

            response.put("jsonErrors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            reservation.setEnabled(true);
            returnedReservation = reservationService.save(reservation);
            //After saving reservation, create its ticket.
            ticket = new Ticket();
            //Set reservation and save ticket.
            ticket.setReservation(returnedReservation);
            ticket = ticketService.save(ticket);

        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "La reservación ha sido realizada éxitosamente!");
        //In this case we just want to return reservation, because it will have the reservation object into it.
        response.put("ticket", ticket);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/reservaciones/{id}")
    public ResponseEntity<?> updateReservation(@Valid @RequestBody Reservation reservation, BindingResult result, @PathVariable Long id){

        Reservation returnedReservation;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()){

            List<String> errors = result.getFieldErrors().stream().map(
                    fieldError -> "Campo " + fieldError.getField() + " " + fieldError.getDefaultMessage()
            ).collect(Collectors.toList());

            response.put("jsonErrors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            returnedReservation = reservationService.findById(id);

            if(returnedReservation == null){
                response.put("mensaje", "Reservación id: " + id + " no existe en la base de datos!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            returnedReservation.setCheckIn(reservation.getCheckIn());
            returnedReservation.setCheckOut(reservation.getCheckOut());
            returnedReservation.setPrice(reservation.getPrice());
            returnedReservation.setRoom(reservation.getRoom());
            returnedReservation.setOfficialDocument(reservation.getOfficialDocument());

            reservationService.save(returnedReservation);

        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un problema consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "La reservacion ha sido actualizada éxitosamente!");
        response.put("reservacion", returnedReservation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/reservaciones/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id){

        Reservation returnedReservation;
        Map<String, Object> response = new HashMap<>();

        try{
            /*
            //Before deleting reservation, also delete its document.
            returnedReservation = reservationService.findById(id);

            String previousDocumentName = returnedReservation.getOfficialDocument();
            uploadService.deleteFile(previousDocumentName);

            reservationService.delete(id);
            */

            /* In this case register will not be deleted, it will change its enabled property and that will be everything. */

            returnedReservation = reservationService.findById(id);
            returnedReservation.setEnabled(false);

            reservationService.save(returnedReservation);

        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un problema consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Reservacion eliminada éxitosamente!");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @RequestMapping("/reservaciones/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id){

        Reservation returnedReservation;
        Map<String, Object> response = new HashMap<>();

        returnedReservation = reservationService.findById(id);

        if(!file.isEmpty()) {
            String fileName = null;
            try{
                fileName = uploadService.copyFile(file);
            } catch (IOException e) {
                response.put("mensaje", "Error al subir documento");
                response.put("error", e.getCause().getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            //This block validates if reservation has uploaded a document previously, then just save the current one and delete the old one.
            String previousDocumentName = returnedReservation.getOfficialDocument();
            uploadService.deleteFile(previousDocumentName);
            //If everything goes well, save filename in customer's photo field.
            returnedReservation.setOfficialDocument(fileName);
            //And save customer with their photo.
            reservationService.save(returnedReservation);

            //Set up response.
            response.put("mensaje", "Haz guardado documento oficial: " + fileName);
            response.put("reservacion", returnedReservation);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/uploads/img/{imageName:.+}")
    public ResponseEntity<Resource> showDocument(@PathVariable String imageName){

        Resource resource = null;

        try {
            resource = uploadService.load(imageName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
