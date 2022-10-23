package com.backendhotelmanagment.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    @Column(name = "generation_date")
    @Temporal(TemporalType.DATE)
    private Date generationDate;

    //Relation between RESERVATIONS and TICKETS, one reservation to one ticket.
    @OneToOne()
    @JoinColumn(name = "idReservation")
    private Reservation reservation;

    //Setters and Getters

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public Date getGenerationDate() {
        return generationDate;
    }

    @PrePersist
    public void setGenerationDate() {
        this.generationDate = new Date();
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
