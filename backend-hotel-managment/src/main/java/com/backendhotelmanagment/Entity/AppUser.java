package com.backendhotelmanagment.Entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "app_users")
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAppUser;

    @NotEmpty(message = "El campo nombre no puede estar vacio.")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "El campo apellido no puede estar vacio.")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "El campo domicilio no puede estar vacio.")
    @Column(name = "address")
    private String address;

    //In this case, email will be used as the username for tokens.
    @Email(message = "El campo email no es correcto.")
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "creation_date")
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @NotEmpty(message = "El campo contraseña no puede estar vacio.")
    @Column(name = "password")
    private String password;

    //This property is just to define if the user is enabled or not.
    //Useful for JWT.
    @Column(name = "enabled", columnDefinition = "tinyint(1) default 1")
    private Boolean enabled;

    /*
    Relations
    */

    //Relation between these two tables, USER and ROLES.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "rel_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}
    )
    private List<Role> roles;

    //Relation between users and social networks.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "rel_users_socialNetworks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "socialNetwork_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "socialNetwork_id"})}
    )
    private List<SocialNetwork> socialNetworks;

    private static final long serialVersionUID = 1L;

    //Setters and Getters.


    public void setCreationDate(){
        this.creationDate = new Date();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Long getId() {
        return idAppUser;
    }

    public void setId(Long id) {
        this.idAppUser = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Boolean getEnabled(){
        return this.enabled;
    }

    @PrePersist
    public void setEnabled(){
        this.enabled = true;
    }

    public List<SocialNetwork> getSocialNetworks() {
        return socialNetworks;
    }

    public void setSocialNetworks(List<SocialNetwork> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }
}
