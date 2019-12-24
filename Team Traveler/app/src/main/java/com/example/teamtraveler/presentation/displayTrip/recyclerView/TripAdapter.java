package com.example.teamtraveler.presentation.displayTrip.recyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamtraveler.R;
import com.example.teamtraveler.Utils.ManipulateDate;
import com.example.teamtraveler.data.entities.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trip> tripList;
    private static TripActionInterface tripActionInterface;

    public TripAdapter(TripActionInterface tripActionInterface) {
        this.tripList = new ArrayList<>();
       this.tripActionInterface = tripActionInterface;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public TripAdapter.TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_trip_item, parent, false);
        TripViewHolder voyageViewHolder = new TripViewHolder(v);
        return voyageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        holder.updateTrip(tripList.get(position));
    }


    @Override
    public int getItemCount() {
        return tripList.size();
    }


    public void bindViewModel(Trip mTrip) {
        this.tripList.add(mTrip);
        notifyDataSetChanged();
    }


    public static class TripViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView nbParticipantTextView;
        private TextView locationTextView;
        private TextView datetextView;
        private View v;


        public TripViewHolder(View v) {
            super(v);
            this.v = v;
            nameTextView = v.findViewById(R.id.name_trip_trip_item);
            datetextView=v.findViewById(R.id.date_trip_trip_item);
            locationTextView=v.findViewById(R.id.location_trip_trip_item);
            nbParticipantTextView = v.findViewById(R.id.nb_partipants_trip_item);
        }

        public void updateTrip(Trip trip) {

            String startDate="";
            String endDate="";
            if (trip.getStartDate()!=null){
                startDate=ManipulateDate.getDateFormatFrench(trip.getStartDate().toString());
            }
            if (trip.getEndDate()!=null){
                endDate= ManipulateDate.getDateFormatFrench(trip.getEndDate().toString());
            }
            nameTextView.setText(trip.getName());
            datetextView.setText("Du " + startDate + " au " + endDate);
            locationTextView.setText("à " + trip.getLocation());
            nbParticipantTextView.setText(String.valueOf(trip.getParticipantsID().size()));

            if(TextUtils.isEmpty(trip.getLocation())){
                locationTextView.setVisibility(View.INVISIBLE);
            }
            setupListeners(trip);
        }

        private void setupListeners(final Trip trip){
           v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tripActionInterface.tripClick(trip.getId());
                }
            });
        }

    }
}

