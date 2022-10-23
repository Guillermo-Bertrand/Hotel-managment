package com.backendhotelmanagment.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rooms")
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRoom;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private float price;

    @Column(name = "room_type")
    private String roomType;

    /*
    Relations
    */

    //Relation between ROOMS and BED-TYPE, many rooms to one room-type.
    @ManyToOne()
    @JoinColumn(name = "id_bed_type")
    private BedType bedType;

    //Setters and Getters

    public Long getIdRoom() {
        return idRoom;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setIdRoom(Long idRoom) {
        this.idRoom = idRoom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public BedType getBedType() {
        return bedType;
    }

    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }
}
