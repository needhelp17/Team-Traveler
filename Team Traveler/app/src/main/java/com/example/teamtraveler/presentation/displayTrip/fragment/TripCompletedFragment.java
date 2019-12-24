package com.example.teamtraveler.presentation.displayTrip.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamtraveler.R;
import com.example.teamtraveler.data.api.services.resultAsynchTaskTrip.ResultAsynchronTaskOneTrip;
import com.example.teamtraveler.data.api.services.TripService;
import com.example.teamtraveler.data.entities.Trip;
import com.example.teamtraveler.presentation.displayTrip.DetailTripActivity;
import com.example.teamtraveler.presentation.displayTrip.recyclerView.TripActionInterface;
import com.example.teamtraveler.presentation.displayTrip.recyclerView.TripAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TripCompletedFragment extends Fragment implements TripActionInterface{
    public static final String TAB_NAME = "Terminées";
    private static TripCompletedFragment INSTANCE = null;
    private View rootView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private TripService tripService;
    private FirebaseAuth firebaseAuth;

    private TripCompletedFragment() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static TripCompletedFragment newInstance() {
        if (INSTANCE==null){
            return new TripCompletedFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_trips_completed, container, false);
        INSTANCE=this;
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = rootView.findViewById(R.id.progress_bar_trips_completed);
        tripService=new TripService();
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view_ended_trip);
        tripAdapter = new TripAdapter(this);
        recyclerView.setAdapter(tripAdapter);
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();

        if(currentUser!=null) {
            progressBar.setVisibility(View.VISIBLE);
            tripService.getCompletedTripsOfParticipant(currentUser.getUid(), new ResultAsynchronTaskOneTrip() {
                @Override
                public void onResponseReceived(Trip response) {
                    if (response != null) {
                        tripAdapter.bindViewModel(response);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void tripClick(String idTrip) {
        Intent intent = new Intent(getActivity().getApplicationContext(), DetailTripActivity.class);
        intent.putExtra(DetailTripActivity.ID_TRIP_DETAIL,idTrip);
        System.out.println("tripClick "+idTrip);
        startActivityForResult(intent,2);
    }
}
