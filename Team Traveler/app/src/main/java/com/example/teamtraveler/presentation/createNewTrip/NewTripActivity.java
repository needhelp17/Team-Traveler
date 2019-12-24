package com.example.teamtraveler.presentation.createNewTrip;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.teamtraveler.R;
import com.example.teamtraveler.Utils.ManipulateDate;
import com.example.teamtraveler.data.api.services.resultAsynchTaskUser.ResultAsynchronTaskUser;
import com.example.teamtraveler.data.api.services.UserService;
import com.example.teamtraveler.data.entities.Trip;
import com.example.teamtraveler.data.entities.User;
import com.example.teamtraveler.presentation.displayTrip.ListTripActivity;
import com.example.teamtraveler.data.api.services.TripService;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Calendar;
import java.util.Date;

public class NewTripActivity extends AppCompatActivity{

    private TripService tripService;
    private Button btnCreateTrip;
    private EditText tripNameEditTxt,tripPlaceEditTxt,tripStartDateEditTxt,tripEndDateEditTxt;
    private FirebaseAuth firebaseAuth;
    private UserService userService;
    private TextInputLayout tripNameWrapper;
    private TextInputLayout tripPlaceWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        tripService =new TripService();
        btnCreateTrip = findViewById(R.id.btn_create_form_trip);
        tripNameEditTxt = findViewById(R.id.name_form_trip);
        tripNameWrapper= (TextInputLayout) findViewById(R.id.name_form_trip_wrapper);
        tripPlaceWrapper = (TextInputLayout) findViewById(R.id.place_form_trip_wrapper);

        tripPlaceEditTxt=findViewById(R.id.place_form_trip);
        setListnerToEditTexts();
        tripStartDateEditTxt=findViewById(R.id.start_date_form_trip);
        tripEndDateEditTxt=findViewById(R.id.end_date_form_trip);
        setEventsToFieldStartDate();
        setEventsToFieldEndDate();
        firebaseAuth=FirebaseAuth.getInstance();
        userService=new UserService();
        View linearLayoutFormCreateTrip=findViewById(R.id.LinearLayoutFormCreateTrip);
        linearLayoutFormCreateTrip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tripNameEditTxt.getWindowToken(), 0);
                return true;
            }
        });
        btnCreateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources resources=getResources();
                if (firebaseAuth.getCurrentUser() != null) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    final Trip trip=checkFieldsAndCreateTrip();
                    if (trip != null) {
                        userService.getUserWithID(currentUser.getUid(), new ResultAsynchronTaskUser() {
                            @Override
                            public void onResponseReceived(User response) {
                                response.addTripId(trip.getId());
                                userService.addUser(response);
                                tripService.addTrip(trip);
                            }
                        });
                        Toast toast = Toast.makeText(view.getContext(), resources.getString(R.string.msg_trip_saved_newTrip), Toast.LENGTH_LONG);
                        toast.show();
                        Intent intent = new Intent(view.getContext(), ListTripActivity.class);
                        startActivity(intent);
                        emptyFields();
                    }
                }
                else{//si l'utilisateur est déconnecté
                    Toast toast = Toast.makeText(view.getContext(),resources.getString(R.string.msg_user_not_connected_newTrip) , Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
    
    private Trip checkFieldsAndCreateTrip() {
        String tripName = tripNameEditTxt.getText().toString();
        String tripPlace = tripPlaceEditTxt.getText().toString();
        String tripStartDateStr = tripStartDateEditTxt.getText().toString();
        String tripEndDateStr = tripEndDateEditTxt.getText().toString();
        Resources resources=getResources();
        Date tripEndDate = null;
        Date tripStartDate = null;
        if (TextUtils.isEmpty(tripName))  {
            Toast toast = Toast.makeText(this.getApplicationContext(), resources.getString(R.string.msg_trip_name_absent), Toast.LENGTH_LONG);
            toast.show();
            tripNameEditTxt.requestFocus(); //donner le focus au champ Nom
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //afficher le clavier
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            return null;
        }
        else if (tripName.length()>20){
            Toast toast = Toast.makeText(this.getApplicationContext(), resources.getString(R.string.msg_trip_name_length_exceed), Toast.LENGTH_LONG);
            toast.show();
            tripNameEditTxt.requestFocus(); //donner le focus au champ Nom
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //afficher le clavier
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            return null;

        }
        else if (tripPlace.length()>20){
            Toast toast = Toast.makeText(this.getApplicationContext(), resources.getString(R.string.msg_trip_place_length_exceed), Toast.LENGTH_LONG);
            toast.show();
            tripPlaceEditTxt.requestFocus(); //donner le focus au champ Nom
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //afficher le clavier
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            return null;

        }
        else {
            tripStartDate =ManipulateDate.strTodDate(tripStartDateStr);
            tripEndDate = ManipulateDate.strTodDate(tripEndDateStr);
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            Trip trip = new Trip(tripName, tripPlace, tripStartDate, tripEndDate, currentUser.getUid());
            return trip;
        }
    }

    private void emptyFields(){
        tripNameEditTxt.setText("");
        tripPlaceEditTxt.setText("");
        tripStartDateEditTxt.setText("");
        tripEndDateEditTxt.setText("");
    }


    public void setListnerToEditTexts(){
        tripNameEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && TextUtils.isEmpty(tripNameEditTxt.getText())){
                    tripNameWrapper.setErrorEnabled(true);
                    tripNameWrapper.setError(getResources().getString(R.string.error_name_required_create_trip));

                }
            }
        });
        tripNameEditTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>20){
                    tripNameWrapper.setErrorEnabled(true);
                    tripNameWrapper.setError(getResources().getString(R.string.error_lenth_name_trip_create_trip));
                }
                else {
                    tripNameWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tripPlaceEditTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>20){
                    tripPlaceWrapper.setErrorEnabled(true);
                    tripPlaceWrapper.setError(getResources().getString(R.string.error_lenth_name_trip_create_trip));
                }
                else {
                    tripPlaceWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    DatePickerDialog.OnDateSetListener onEndDateChangeListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            tripEndDateEditTxt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };
    DatePickerDialog.OnDateSetListener onStartDateChangeLinstener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            tripStartDateEditTxt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            String startDateStr=tripStartDateEditTxt.getText().toString();
            String endDateStr=tripEndDateEditTxt.getText().toString();
            if (!TextUtils.isEmpty(endDateStr)) {
                Date startDate=ManipulateDate.strTodDate(startDateStr);
                Date endDate=ManipulateDate.strTodDate(endDateStr);
                if (endDate.before(startDate)){//si la date de fin est inférieur à la date de début on lui attribut la date de début
                    tripEndDateEditTxt.setText(startDateStr);
                }
            }
        }
    };


    private void setEventsToFieldEndDate(){
        final Calendar calendar = Calendar.getInstance();
        final long currentTime=calendar.getTimeInMillis();
        tripEndDateEditTxt.setInputType(InputType.TYPE_NULL);
        tripEndDateEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(tripStartDateEditTxt.getText().toString())){
                        showCalender(tripEndDateEditTxt, currentTime,onEndDateChangeListener);
                    }
                    else {
                        long timeStartDate =ManipulateDate.getTimeOfDate(tripStartDateEditTxt.getText().toString());
                        showCalender(tripEndDateEditTxt, timeStartDate,onEndDateChangeListener);
                    }
                }
            }
        });
        tripEndDateEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(tripStartDateEditTxt.getText().toString())){
                    showCalender(tripEndDateEditTxt, currentTime,onEndDateChangeListener);
                }
                else {
                    long timeStartDate =ManipulateDate.getTimeOfDate(tripStartDateEditTxt.getText().toString());
                    showCalender(tripEndDateEditTxt, timeStartDate,onEndDateChangeListener);
                }
            }
        });
    }

    private void setEventsToFieldStartDate(){
        final Calendar calendar = Calendar.getInstance();
        final long currentTime=calendar.getTimeInMillis();
        tripStartDateEditTxt.setInputType(InputType.TYPE_NULL);
        tripStartDateEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showCalender(tripStartDateEditTxt, currentTime,onStartDateChangeLinstener);
                }
            }
        });
        tripStartDateEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(tripStartDateEditTxt,currentTime,onStartDateChangeLinstener);
            }
        });
    }


    private void showCalender(final EditText editText,long minDate,DatePickerDialog.OnDateSetListener onDateSetListener){
        DatePickerDialog picker;
        int day;
        int month;
        int year;
        int[] date = ManipulateDate.parseDate(editText.getText().toString());
        day = date[0];
        month =date[1];
        year = date[2];
        picker = new DatePickerDialog(NewTripActivity.this,R.style.DialogDateTheme, onDateSetListener, year, month, day);
        picker.getDatePicker().setMinDate(minDate);
        picker.show();
    }

}
