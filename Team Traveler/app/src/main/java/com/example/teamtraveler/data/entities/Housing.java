package com.example.teamtraveler.data.entities;

import java.util.UUID;

public class Housing {
    private String id;
    private String name;
    private String price;
    private String nbRoom;
    private String nbBathRoom;
    private String description;
    private String idTrip;
    public Housing(String name,String price,String nbRoom, String nbBathRoom,String description,String idTrip) {
        this.price = price;
        this.name=name;
        this.price=price;
        this.nbRoom=nbRoom;
        this.nbBathRoom=nbBathRoom;
        this.description=description;
        this.idTrip=idTrip;
        this.id= UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNbRoom() {
        return nbRoom;
    }

    public void setNbRoom(String nbRoom) {
        this.nbRoom = nbRoom;
    }

    public String getNbBathRoom() {
        return nbBathRoom;
    }

    public void setNbBathRoom(String nbBathRoom) {
        this.nbBathRoom = nbBathRoom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(String idTrip) {
        this.idTrip = idTrip;
    }
}
