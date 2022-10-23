package com.backendhotelmanagment.Controller;

import com.backendhotelmanagment.Entity.AppUser;
import com.backendhotelmanagment.Service.AppUserService;
import com.backendhotelmanagment.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class AppUserRestController {

    private final AppUserService appUserService;
    private final MailService mailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AppUserRestController(AppUserService appUserService, MailService mailService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.appUserService = appUserService;
        this.mailService = mailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //Method to recover a user's password.
    @PostMapping("/usuarios/recuperar")
    public ResponseEntity<?> recoverPassword(@RequestParam("to") String to){

        AppUser returnedAppUser;
        Map<String, Object> response = new HashMap<>();

        try{//First get this user by given email.
            returnedAppUser = appUserService.findAppUserByEmail(to);

            //If user doesn't exist, send response to user.
            if(returnedAppUser == null){
                response.put("mensaje", "Usuario con email: " + to + " no existe en la base de datos!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            //Otherwise, reply with her or his password.
            String subject = "Recuperación de contraseña";
            String text = "Hola " + returnedAppUser.getName() + "!\n\nEsta es tu contraseña: " + returnedAppUser.getPassword();

            mailService.sendMail("bris4.m4rina@gmail.com", to, subject, text);

        }catch (MailException e){
            response.put("mensaje", "Hubo un error al enviar el email!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El correo ha sido enviado!");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /*These both methods showUsers and showUser will be used for administrator, for example if the want to see who are employees, not for log in*/
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/usuarios")
    public List<AppUser> showUsers(){
        return appUserService.findAll();
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> showAppUser(@PathVariable Long id){

        AppUser appUser;
        Map<String, Object> response = new HashMap<>();

        try{
            appUser = appUserService.findById(id);

            if(appUser == null){
                response.put("mensaje", "Usuario id: " + id + " no existe en la base de datos!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(appUser, HttpStatus.OK);
    }

    //This method will be used to create new users, but just and admin will be able to do it, which means, it is the main controller's function.
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@Valid @RequestBody AppUser appUser, BindingResult result){

        AppUser returnedAppUser;
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
            //Before saving user, encrypt his or her password.
            appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
            //And then save it.
            returnedAppUser = appUserService.save(appUser);

        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un error consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El usuario ha sido creado exitosamente!");
        response.put("Usuario", returnedAppUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> updateAppUser(@Valid @RequestBody AppUser appUser, BindingResult result, @PathVariable Long id){

        AppUser returnedAppUser;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()){

            List<String> errors = result.getFieldErrors().stream().map(
                    fieldError -> "Campo " + fieldError.getField() + " " + fieldError.getDefaultMessage()
            ).collect(Collectors.toList());

            response.put("jsonErrors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try{

            returnedAppUser = appUserService.findById(id);

            if(returnedAppUser == null){
                response.put("mensaje", "Usuario id: " + id + " no existe en la base de datos!");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            returnedAppUser.setName(appUser.getName());
            returnedAppUser.setLastName(appUser.getLastName());
            returnedAppUser.setAddress(appUser.getAddress());
            returnedAppUser.setEmail(appUser.getEmail());
            returnedAppUser.setPassword(appUser.getPassword());
            returnedAppUser.setRoles(appUser.getRoles());
            returnedAppUser.setSocialNetworks(appUser.getSocialNetworks());

            appUserService.save(returnedAppUser);

        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un problema consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El usuario ha sido actualizado exitosamente!");
        response.put("usuario", returnedAppUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> deleteAppUser(@PathVariable Long id){

        Map<String, Object> response = new HashMap<>();

        try{
            appUserService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Hubo un problema consultando la base de datos!");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Usuario eliminado exitosamente!");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
