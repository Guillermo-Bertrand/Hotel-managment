package com.backendhotelmanagment.Controller;

import com.backendhotelmanagment.Entity.Cancellation;
import com.backendhotelmanagment.Entity.Reservation;
import com.backendhotelmanagment.Service.CancellationService;
import com.backendhotelmanagment.Service.ReservationService;
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
public class CancellationRestController {

    private final CancellationService cancellationService;
    private final ReservationService reservationService;

    @Autowired
    public CancellationRestController(CancellationService cancellationService, ReservationService reservationService){
        this.cancellationService = cancellationService;
        this.reservationService = reservationService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/cancelaciones")
    public List<Cancellation> showCancellations(){
        return cancellationService.findAll();
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/cancelaciones/{id}")
    public ResponseEntity<?> showCancellation(@PathVariable Long id){

        Cancellation cancellation;
        Map<String, Object> response = new HashMap<>();

        try{
            cancellation = cancellationService.findById(id);

            if(cancellation == null){
                response.put("mensaje", "Cancelacion id: " + id + " no existe en la base de datos!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(cancellation, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/cancelaciones")
    public ResponseEntity<?> addCancellation(@Valid @RequestBody Cancellation cancellation, BindingResult result){

        Cancellation returnedCancellation = null;
        Reservation returnedReservation;
        Map<String, Object> response = new HashMap<>();

        //If there are any errors coming from request, gather and return them in responseEntity.
        if(result.hasErrors()){
            //Map fieldErrors to transform every fieldError in string, it's similar to javaScript.
            List<String> errors = result.getFieldErrors().stream().map(
                    error -> "Campo " + error.getField() + " " + error.getDefaultMessage()
            ).collect(Collectors.toList());

            response.put("jsonErrors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{
            //In this case it will do 2 things first change reservation's enabled property to false.

            returnedReservation = cancellation.getReservation();
            returnedReservation.setEnabled(false);
            reservationService.save(returnedReservation);

            //And then set updated reservation to cancellation to finally save cancellation register.

            cancellation.setReservation(returnedReservation);
            returnedCancellation = cancellationService.save(cancellation);

        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La cancelacion ha sido realizada éxitosamente!");
        response.put("cancelacion", returnedCancellation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/cancelaciones/{id}")
    public ResponseEntity<?> updateCancellation(@Valid @RequestBody Cancellation cancellation, BindingResult result, @PathVariable Long id){

        Cancellation returnedCancellation;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()){

            List<String> errors = result.getFieldErrors().stream().map(
                    fieldError -> "Campo " + fieldError.getField() + " " + fieldError.getDefaultMessage()
            ).collect(Collectors.toList());

            response.put("jsonErrors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{

            returnedCancellation = cancellationService.findById(id);

            if(returnedCancellation == null){
                response.put("mensaje", "Cancelacion id: " + id + " no existe en la base de datos!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            returnedCancellation.setRefund(cancellation.getRefund());

            cancellationService.save(returnedCancellation);

        }catch(DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La cancelacion ha sido actualizada éxitosamente!");
        response.put("cancelacion", returnedCancellation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/cancelaciones/{id}")
    public ResponseEntity<?> deleteCancellation(@PathVariable Long id){

        Map<String, Object> response = new HashMap<>();

        try{
            cancellationService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Cancelacion eliminada éxitosamente!");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
