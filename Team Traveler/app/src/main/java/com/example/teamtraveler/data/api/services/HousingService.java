package com.example.teamtraveler.data.api.services;

import android.util.Log;

import androidx.annotation.NonNull;
import com.example.teamtraveler.data.api.services.resultAsynchTaskHousing.ResultAsynchronTaskOneHousing;
import com.example.teamtraveler.data.api.services.resultAsynchTaskTrip.ResultAsynchronTaskOneTrip;
import com.example.teamtraveler.data.entities.Housing;
import com.example.teamtraveler.data.entities.Trip;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class HousingService {
    private static final String TAG_ADD = "add housing";
    private static final String TAG_GET_ALL = "all housing of trip";
    private static final String TAG_GET_HOUSE = "get housing with id";
    private CollectionReference collectionReferenceHousings;
    private TripService tripService;

    public HousingService(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        this.collectionReferenceHousings=database.collection("Housings");
        tripService=new TripService();
    }

    public void addHousing(Housing housing){
        collectionReferenceHousings.document(housing.getId()).set(housing).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG_ADD,"Success the housing is added ");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_ADD,"Error the housing does not added");
                    }
                });
    }

    public void getHousingWithID(String id,final ResultAsynchronTaskOneHousing resultAsynchronTask){
        DocumentReference docRef=collectionReferenceHousings.document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG_GET_HOUSE,documentSnapshot.toString());
                Housing housing = documentSnapshot.toObject(Housing.class);
                resultAsynchronTask.onResponseReceived(housing);
            }
        });
    }

    public void getHousingOfTrip(final String tripId, final ResultAsynchronTaskOneHousing resultAsynchronTask){
        tripService.getTripWithID(tripId, new ResultAsynchronTaskOneTrip() {
            @Override
            public void onResponseReceived(Trip trip) {
                List<String> housingId= trip.getHousingsId();
                for (int i=0;i<housingId.size();i++){
                    getHousingWithID(housingId.get(i), new ResultAsynchronTaskOneHousing() {
                        @Override
                        public void onResponseReceived(Housing response) {
                                    resultAsynchronTask.onResponseReceived(response);
                        }
                    });
                }
            }
        });
    }

}