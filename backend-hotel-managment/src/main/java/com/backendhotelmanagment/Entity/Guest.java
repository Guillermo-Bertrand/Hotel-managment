package com.backendhotelmanagment.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGuest;

    @NotEmpty(message = "El campo nombre de huesped no puede estar vacio.")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "El campo apellido de huesped no puede estar vacio,")
    @Column(name = "last_name")
    private String lastName;

    //Relation between RESERVATIONS and GUESTS, many guests to a single reservation
    @ManyToOne()
    @JoinColumn(name = "idReservation")
    private Reservation reservation;

    public Long getIdGuest() {
        return idGuest;
    }

    public void setIdGuest(Long idGuest) {
        this.idGuest = idGuest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
