package com.example.teamtraveler.presentation.displayTrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.teamtraveler.R;
import com.example.teamtraveler.Utils.ManipulateDate;
import com.example.teamtraveler.data.api.services.resultAsynchTaskTrip.ResultAsynchronTaskOneTrip;
import com.example.teamtraveler.data.api.services.TripService;
import com.example.teamtraveler.data.entities.Trip;
import com.example.teamtraveler.presentation.homePage.HomePageActivity;
import com.example.teamtraveler.presentation.housing.NewHousingActivity;
import com.example.teamtraveler.presentation.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DetailTripActivity extends AppCompatActivity {
    public final static String ID_TRIP_DETAIL="com.example.teamtraveler.presentation.displayTrip.DetailTripActivity.ID_TRIP_DETAIL";
    public final static String NAME_TRIP_DETAIL="com.example.teamtraveler.presentation.displayTrip.DetailTripActivity.NAME_TRIP_DETAIL";
    public final static String PLACE_TRIP_DETAIL="com.example.teamtraveler.presentation.displayTrip.DetailTripActivity.PLACE_TRIP_DETAIL";
    public final static String DATE_START_TRIP_DETAIL="com.example.teamtraveler.presentation.displayTrip.DetailTripActivity.DATE_START_TRIP_DETAIL";
    public final static String DATE_END_TRIP_DETAIL="com.example.teamtraveler.presentation.displayTrip.DetailTripActivity.DATE_END_TRIP_DETAIL";

    private FirebaseAuth firebaseAuth;
    private TextView tripNameTxtView,tripPlaceTxtView,tripDateTxtView;
    private Button btnHousing;
    private String idTrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip);
        firebaseAuth=FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar_detail_trip);
        setSupportActionBar(toolbar);
        tripNameTxtView=findViewById(R.id.name_trip_detail);
        tripPlaceTxtView=findViewById(R.id.place_trip_detail);
        tripDateTxtView=findViewById(R.id.date_trip_detail);
        Intent intent=getIntent();
        this.idTrip=intent.getStringExtra(ID_TRIP_DETAIL);
        TripService tripService=new TripService();
        btnHousing=findViewById(R.id.btn_housing_detail);
        btnHousing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewHousingActivity.class);
                intent.putExtra(NewHousingActivity.ID_TRIP_NEW_HOUSING,idTrip);
                startActivity(intent);
            }
        });
        tripService.getTripWithID(idTrip, new ResultAsynchronTaskOneTrip() {
            @Override
            public void onResponseReceived(Trip trip) {
                String tripStartDate="";
                String tripEndDate="";
                if(trip.getStartDate()!=null){
                    tripStartDate=ManipulateDate.getDateFormatFrench(trip.getStartDate().toString());
                }
                if(trip.getEndDate()!=null) {
                    tripEndDate = ManipulateDate.getDateFormatFrench(trip.getEndDate().toString());
                }
                tripNameTxtView.setText(trip.getName());
                tripPlaceTxtView.setText("A "+trip.getLocation());

                tripDateTxtView.setText("Du "+ tripStartDate+ " au "+tripEndDate);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            getMenuInflater().inflate(R.menu.menu_user_online, menu);
        }else {
            getMenuInflater().inflate(R.menu.menu_user_offline, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_connexion) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
        }
        else if(id == R.id.action_log_off){
            firebaseAuth.signOut();
            Toast toast = Toast.makeText(this, "Vous etes déconnecté", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(this, HomePageActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
