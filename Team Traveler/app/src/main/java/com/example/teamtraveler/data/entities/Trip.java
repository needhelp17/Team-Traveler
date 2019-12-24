package com.example.teamtraveler.data.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Trip {
    private String id;
    private String name;
    private String location;
    private Date startDate;
    private Date endDate;
    private String idOrganizer;
    private List<String> participantsID;
    private List<String> housingsId;
    public Trip(String name, String location, Date startDate, Date endDate, String idOrganizer) {
        this.name=name;
        this.location=location;
        this.startDate=startDate;
        this.endDate=endDate;
        this.idOrganizer=idOrganizer;
        this.id= UUID.randomUUID().toString();
        this.participantsID=new ArrayList<>();
        this.participantsID.add(idOrganizer);
        this.housingsId=new ArrayList<>();
    }

    public Trip() {
     
    }

    public Trip(String name , List<String> participants){
        this.name=name;
        this.participantsID=participants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOrganizer() {
        return idOrganizer;
    }

    public void setOrganizer(String idOrganizer) {
        this.idOrganizer = idOrganizer;
    }

    public String getId() {
        return id;
    }

    public List<String> getParticipantsID() {
        return participantsID;
    }

    public void setParticipantsID(List<String> participantsID) {
        this.participantsID = participantsID;
    }

    public List<String> getHousingsId() {
        return housingsId;
    }

    public void setHousingsId(List<String> housingsID) {
        this.housingsId = housingsID;
    }
    public void addHousingId(String housingId){
        this.housingsId.add(housingId);
    }

}
