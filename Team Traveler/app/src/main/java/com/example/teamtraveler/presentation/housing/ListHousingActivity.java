package com.example.teamtraveler.presentation.housing;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teamtraveler.R;
import com.example.teamtraveler.data.api.services.HousingService;
import com.example.teamtraveler.data.api.services.TripService;
import com.google.android.material.textfield.TextInputLayout;

public class ListHousingActivity extends AppCompatActivity {
    public final static String ID_TRIP_NEW_HOUSING="com.example.teamtraveler.presentation.housing.NewHousingActivity.ID_TRIP_NEW_HOUSING";
    private HousingService housingService;
    private TripService tripService;
    private EditText nameEditText,priceEditText, nbRoomEditText, nbBathRoomEditText,descriptionEditText;
    private TextInputLayout nameWrapper,priceWrapper,nbRoomWrapper,nbBathRoomWrapper,descriptionWrapper;
    private Button btnCreate;
    private String idTrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_housing);
    }
}
