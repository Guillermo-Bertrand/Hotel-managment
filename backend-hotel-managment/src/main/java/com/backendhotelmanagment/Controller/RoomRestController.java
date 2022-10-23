package com.backendhotelmanagment.Controller;

import com.backendhotelmanagment.Entity.Room;
import com.backendhotelmanagment.Entity.BedType;
import com.backendhotelmanagment.Service.RoomService;
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
public class RoomRestController {

    private final RoomService roomService;

    @Autowired
    public RoomRestController(RoomService roomService){
        this.roomService = roomService;
    }

    @GetMapping("/habitaciones")
    public List<Room> showRoomTypes(){
        return roomService.findAllRoomTypes();
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/registro-habitaciones")
    public List<Room> showRooms(){
        return roomService.findAll();
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/registro-habitaciones/{id}")
    public ResponseEntity<?> showRoom(@PathVariable Long id){

        Room room;
        Map<String, Object> response = new HashMap<>();

        try{
            room = roomService.findById(id);

            if(room == null){
                response.put("mensaje", "Cuarto id: " + id + " no existe en la base de datos!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/registro-habitaciones")
    public ResponseEntity<?> addRoom(@Valid @RequestBody Room room, BindingResult result){

        Room returnedRoom;
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
            returnedRoom = roomService.save(room);
        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La habitacion ha sido guardada éxitosamente!");
        response.put("habitacion", returnedRoom);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/registro-habitaciones/{id}")
    public ResponseEntity<?> updateRoom(@Valid @RequestBody Room room, BindingResult result, @PathVariable Long id){

        Room returnedRoom;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()){

            List<String> errors = result.getFieldErrors().stream().map(
                    fieldError -> "Campo " + fieldError.getField() + " " + fieldError.getDefaultMessage()
            ).collect(Collectors.toList());

            response.put("jsonErrors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{

            returnedRoom = roomService.findById(id);

            if(returnedRoom == null){
                response.put("mensaje", "Habitacion id: " + id + " no existe en la base de datos!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            returnedRoom.setBedType(room.getBedType());
            returnedRoom.setDescription(room.getDescription());
            returnedRoom.setPrice(room.getPrice());
            returnedRoom.setRoomType(room.getRoomType());

            roomService.save(returnedRoom);

        }catch(DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La habitacion ha sido actualizada éxitosamente!");
        response.put("habitacion", returnedRoom);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/registro-habitaciones/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id){

        Map<String, Object> response = new HashMap<>();

        try{
            roomService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Habitacion eliminada éxitosamente!");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}





















