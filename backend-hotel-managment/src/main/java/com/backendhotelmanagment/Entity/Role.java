package com.backendhotelmanagment.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;

    /*
    In this case is not necessary to create a double way relation because
    we are interested in getting roles that a user could have, not roles
    that are related to specific users.

    But if needed would be as follows:

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
    */

    @Column(name = "role", nullable = false)
    private String role;

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
