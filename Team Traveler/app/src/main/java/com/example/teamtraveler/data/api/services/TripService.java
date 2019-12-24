package com.example.teamtraveler.data.api.services;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.teamtraveler.Utils.ManipulateDate;
import com.example.teamtraveler.data.api.services.resultAsynchTaskTrip.ResultAsynchronTaskListTrip;
import com.example.teamtraveler.data.api.services.resultAsynchTaskTrip.ResultAsynchronTaskOneTrip;
import com.example.teamtraveler.data.api.services.resultAsynchTaskUser.ResultAsynchronTaskUser;
import com.example.teamtraveler.data.entities.Trip;
import com.example.teamtraveler.data.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripService {
    private static final String TAG_ADD = "add trip";
    private static final String TAG_GET_ALL = "all trips";
    private static final String TAG_GET_TRIP = "Trip with id";
    private CollectionReference collectionReferenceTrips;
    private UserService userService;

    public TripService(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        this.collectionReferenceTrips=database.collection("Voyages");
        userService=new UserService();

    }

    public void getTrips(final ResultAsynchronTaskListTrip resultAsynchronTask){
        final List<Trip> listVoyage =new ArrayList<Trip>();
        this.collectionReferenceTrips.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listVoyage.add(document.toObject(Trip.class));
                                Log.d(TAG_GET_ALL, document.getId() + " => " + document.getData());

                            }
                            resultAsynchronTask.onResponseReceived(listVoyage);
                        } else {
                            Log.w(TAG_GET_ALL, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void addHousingToTrip(String idTrip, final String housingId){
        final DocumentReference docRef=collectionReferenceTrips.document(idTrip);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Trip trip = documentSnapshot.toObject(Trip.class);
                trip.addHousingId(housingId);

                docRef.update("housingsId", trip.getHousingsId());
            }
        });
    }

    public void addTrip(Trip voyage){
        collectionReferenceTrips.document(voyage.getId()).set(voyage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG_ADD,"Success the trip is added ");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG_ADD,"Error the trip does not added");
            }
        });
    }

    public void getTripWithID(String id,final ResultAsynchronTaskOneTrip resultAsynchronTask){

        DocumentReference docRef=collectionReferenceTrips.document(id);

                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG_ADD,documentSnapshot.toString());
                           Trip trip = documentSnapshot.toObject(Trip.class);
                        resultAsynchronTask.onResponseReceived(trip);
                    }
                });
    }



    public void getTripsOfParticipant(final String userID, final ResultAsynchronTaskOneTrip resultAsynchronTask){

        userService.getUserWithID(userID, new ResultAsynchronTaskUser() {
            @Override
            public void onResponseReceived(User response) {
               List<String> tripsId= response.getTripsID();

               for (int i=0;i<tripsId.size();i++){
                getTripWithID(tripsId.get(i), new ResultAsynchronTaskOneTrip() {
                    @Override
                    public void onResponseReceived(Trip response) {
                        if (response.getEndDate() != null) {
                            if (ManipulateDate.compareDate(response.getEndDate(),new Date())>=0) {
                                resultAsynchronTask.onResponseReceived(response);
                            }
                        }
                        else
                        {
                            resultAsynchronTask.onResponseReceived(response);
                        }
                    }
                });
               }
            }
        });
    }


    public void getTripsOfParticipantBis(final String userID, final ResultAsynchronTaskListTrip resultAsynchronTask){
        getTrips(new ResultAsynchronTaskListTrip() {
            @Override
            public void onResponseReceived(List<Trip> response) {
                List<Trip> trips=new ArrayList<>();
                for(Trip trip : response){
                    List<String> participants=trip.getParticipantsID();
                    for(int i=0;i<participants.size();i++){
                        if(participants.get(i).equals(userID)){
                            trips.add(trip);
                        }
                    }
                }
                resultAsynchronTask.onResponseReceived(trips);
            }

        });

    }

    public void getCompletedTripsOfParticipant(String userID,final ResultAsynchronTaskOneTrip resultAsynchronTaskOneTrip) {
        userService.getUserWithID(userID, new ResultAsynchronTaskUser() {
            @Override
            public void onResponseReceived(User response) {
                List<String> tripsId= response.getTripsID();
                for (int i = 0; i < tripsId.size(); i++) {
                        getTripWithID(tripsId.get(i), new ResultAsynchronTaskOneTrip() {
                            @Override
                            public void onResponseReceived(Trip response) {
                                if (response.getEndDate() != null) {
                                    if (ManipulateDate.compareDate(response.getEndDate(),new Date())<0) {
                                        resultAsynchronTaskOneTrip.onResponseReceived(response);
                                    }
                                    else {
                                        resultAsynchronTaskOneTrip.onResponseReceived(null);
                                    }
                                }
                            }
                        });
                    }
                }

        });
    }
}
