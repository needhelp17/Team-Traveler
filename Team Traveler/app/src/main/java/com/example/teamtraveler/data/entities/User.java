package com.example.teamtraveler.data.entities;

import java.util.List;
import java.util.UUID;

public class User {
    private String id;
    private String name;
    private String email;
    private String urlProfileImage;
    private List<String> tripsID;

    public User(String name, String email, String id) {
        this.id = id;
        this.name = name;
        this.email = email;

    }
    public User(){

    }
    public String getId() {
        return id;
    }

    // on doit pas pouvoir le modifier JAMAIS
    //public void setId(String id) {
    //    this.id = id;
    //}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<String> getTripsID() {
        return tripsID;
    }

    public void setTripsID(List<String> tripsListID) {
        this.tripsID = tripsListID;
    }

    public String getUrlProfileImage() {
        return urlProfileImage;
    }

    public void setUrlProfileImage(String urlProfileImage) {
        this.urlProfileImage = urlProfileImage;
    }

    public void addTripId(String id) {
        tripsID.add(id);
    }
}
