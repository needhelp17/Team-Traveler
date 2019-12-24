package com.example.teamtraveler.data.api.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.teamtraveler.data.api.services.resultAsynchTaskUser.ResultAsynchronTaskUser;
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
import java.util.List;

public class UserService {

    private static final String TAG_ADD = "add particpant";
    private static final String TAG_GET_ALL = "all particpants";
    private static final String TAG_GET_USER = "User with id";
    private CollectionReference collectionReference;
    private User participant=null;
    public UserService(){
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        this.collectionReference=database.collection("Users");
    }

    public List<User> getUsers(){
        final List<User> listParticipant=new ArrayList<User>();
        this.collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listParticipant.add(document.toObject(User.class));
                                Log.d(TAG_GET_ALL, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG_GET_ALL, "Error getting documents.", task.getException());
                        }
                    }
                });
        return listParticipant;
    }

    public void addUser(User profil){
        this.collectionReference.document(profil.getId()).set(profil).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG_ADD,"Success the profil is added ");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG_ADD,"Error the profil does not added");
                    }
                });
    }

    public void getUserWithID(String id,final ResultAsynchronTaskUser resultAsynchronTasKUser){

        DocumentReference docRef=this.collectionReference.document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG_GET_USER,documentSnapshot.toString());
                participant = documentSnapshot.toObject(User.class);
                resultAsynchronTasKUser.onResponseReceived(participant);
            }
        });

    }


}
