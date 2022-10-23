package com.backendhotelmanagment.Controller;

import com.backendhotelmanagment.Entity.Guest;
import com.backendhotelmanagment.Entity.Reservation;
import com.backendhotelmanagment.Entity.Ticket;
import com.backendhotelmanagment.Service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin({"http://localhost:4200", "https://angular-hotel-managment.web.app"})
public class GuestRestController {

    private final GuestService guestService;

    @Autowired
    public GuestRestController(GuestService guestService){
        this.guestService = guestService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/huespedes")
    public ResponseEntity<?> addGuest(@Valid @RequestBody Guest guest, BindingResult result){

        Guest returnedGuest;
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
            returnedGuest = guestService.save(guest);
        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El huésped ha sido almacenado éxitosamente!");
        //In this case we just want to return reservation, because it will have the reservation object into it.
        response.put("huesped", returnedGuest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/huespedes/{id}")
    public ResponseEntity<?> updateReservation(@Valid @RequestBody Guest guest, BindingResult result, @PathVariable Long id){

        Guest returnedGuest;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()){

            List<String> errors = result.getFieldErrors().stream().map(
                    fieldError -> "Campo " + fieldError.getField() + " " + fieldError.getDefaultMessage()
            ).collect(Collectors.toList());

            response.put("jsonErrors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            returnedGuest = guestService.findById(id);

            if(returnedGuest == null){
                response.put("mensaje", "Huesped id: " + id + " no existe en la base de datos!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            returnedGuest.setName(guest.getName());
            returnedGuest.setLastName(guest.getLastName());
            returnedGuest.setReservation(guest.getReservation());

            guestService.save(returnedGuest);

        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un problema consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El huesped ha sido actualizado éxitosamente!");
        response.put("huesped", returnedGuest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
