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
import com.example.teamtraveler.presentation.createNewTrip.NewTripActivity;
import com.example.teamtraveler.presentation.displayTrip.DetailTripActivity;
import com.example.teamtraveler.presentation.displayTrip.recyclerView.TripActionInterface;
import com.example.teamtraveler.presentation.displayTrip.recyclerView.TripAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TripInProgressFragment extends Fragment implements TripActionInterface {

    public static final String TAB_NAME = "EN COURS";
    private static TripInProgressFragment INSTANCE = null;
    private View rootView;
    private ProgressBar progressBar;
    private FloatingActionButton btnNewTrip;
    private RecyclerView recyclerView;
    private TripAdapter tripAdapter;
    private TripService tripService;
    private  FirebaseAuth firebaseAuth;

    private TripInProgressFragment() {
         firebaseAuth = FirebaseAuth.getInstance();
    }

    public static TripInProgressFragment newInstance() {
        if (INSTANCE==null){
            return new TripInProgressFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_trips_in_progress, container, false);
        INSTANCE=this;
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = rootView.findViewById(R.id.progress_bar_trips_in_progress);
        tripService=new TripService();
        setupRecyclerView();
        btnNewTrip=(FloatingActionButton)getActivity().findViewById(R.id.btn_new_trip_trips_progress);
        btnNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewTripActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view_trip);

        tripAdapter = new TripAdapter(this);
        recyclerView.setAdapter(tripAdapter);
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();

        if(currentUser!=null) {
            progressBar.setVisibility(View.VISIBLE);
            tripService.getTripsOfParticipant(currentUser.getUid(), new ResultAsynchronTaskOneTrip() {
                @Override
                public void onResponseReceived(Trip response) {
                    tripAdapter.bindViewModel(response);
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
