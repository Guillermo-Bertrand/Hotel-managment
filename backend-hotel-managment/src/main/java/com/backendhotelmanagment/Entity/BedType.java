package com.backendhotelmanagment.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bed_types")
public class BedType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBedType;

    @Column(name = "type")
    private String type;

    //Setters and Getters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getIdBedType() {
        return idBedType;
    }

    public void setIdBedType(Long idRoomType) {
        this.idBedType = idRoomType;
    }
}
