package com.backendhotelmanagment.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cancellations")
public class Cancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCancellation;

    @Column(name = "refund")
    private Float refund;

    @Column(name = "date")
    private Date date;

    /*Relations*/

    @OneToOne()
    @JoinColumn(name = "idReservation")
    private Reservation reservation;

    //Setters and Getters

    public Long getIdCancellation() {
        return idCancellation;
    }

    public void setIdCancellation(Long idCancellation) {
        this.idCancellation = idCancellation;
    }

    public Float getRefund() {
        return refund;
    }

    public void setRefund(Float refund) {
        this.refund = refund;
    }

    public Date getDate() {
        return date;
    }

    @PrePersist
    public void setDate() {
        this.date = new Date();
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
