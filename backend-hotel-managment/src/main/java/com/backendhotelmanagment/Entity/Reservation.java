package com.backendhotelmanagment.Entity;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;

    @Column(name = "check_in")
    @Temporal(TemporalType.DATE)
    private Date checkIn;

    @Column(name = "check_out")
    @Temporal(TemporalType.DATE)
    private Date checkOut;

    @Column(name = "price")
    private Float price;

    @Column(name = "official_document")
    private String officialDocument;

    //This column will define if reservation has been cancelled.
    @Column(name = "enabled", columnDefinition = "tinyint(1) default 1")
    private Boolean enabled;

    /*Relations*/

    //Relation between RESERVATIONS and ROOMS, one reservation to one room.
    @OneToOne()
    @JoinColumn(name = "idRoom")
    private Room room;

    //Relation between RESERVATIONS and PaymentMethod, one reservation to one method.
    @OneToOne()
    @JoinColumn(name = "id_payment_method")
    private PaymentMethod paymentMethod;

    private static final long serialVersionUID = 1L;

    /*Setters and Getters*/

    public Long getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getOfficialDocument() {
        return officialDocument;
    }

    public void setOfficialDocument(String officialDocument) {
        this.officialDocument = officialDocument;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
