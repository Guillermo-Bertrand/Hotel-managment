package com.backendhotelmanagment.Entity;

import javax.persistence.*;

@Entity
@Table(name = "social_networks")
public class SocialNetwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSocialNetwork;

    @Column(name = "social_network")
    private String socialNetwork;

    //Setters and Getters

    public Long getIdSocialNetwork() {
        return idSocialNetwork;
    }

    public void setIdSocialNetwork(Long idSocialNetwork) {
        this.idSocialNetwork = idSocialNetwork;
    }

    public String getSocialNetwork(){
        return this.socialNetwork;
    }

    public void setSocialNetwork(String socialNetwork){
        this.socialNetwork = socialNetwork;
    }
}
