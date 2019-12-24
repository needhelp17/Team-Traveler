package com.example.teamtraveler.presentation.housing;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.teamtraveler.R;
import com.example.teamtraveler.data.api.services.HousingService;
import com.example.teamtraveler.data.api.services.TripService;
import com.example.teamtraveler.data.entities.Housing;
import com.google.android.material.textfield.TextInputLayout;

public class NewHousingActivity extends AppCompatActivity {
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
        nameEditText=findViewById(R.id.name_form_housing);
        nbBathRoomEditText=findViewById(R.id.nb_bathroom_form_housing);
        nbRoomEditText=findViewById(R.id.nb_room_form_housing);
        descriptionEditText=findViewById(R.id.description_form_housing);
        priceEditText=findViewById(R.id.price_form_housing);

        nameWrapper=findViewById(R.id.name_form_housing_wrapper);
        priceWrapper=findViewById(R.id.price_form_housing_wrapper);
        nbRoomWrapper=findViewById(R.id.nb_room_form_housing_wrapper);
        nbBathRoomWrapper=findViewById(R.id.nb_bathroom_form_housing_wrapper);
        descriptionWrapper=findViewById(R.id.description_form_housing_wrapper);
        View linearLayoutFormCreateTrip=findViewById(R.id.LinearLayout_new_housing);
        linearLayoutFormCreateTrip.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);
                return true;
            }
        });
        setListnerToEditTexts();
        Intent intent=getIntent();
        housingService=new HousingService();
        tripService=new TripService();
        idTrip=intent.getStringExtra(ID_TRIP_NEW_HOUSING);
        btnCreate=findViewById(R.id.btn_create_form_housing);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Housing housing=checkFields();
                if (housing!=null) {
                    housingService.addHousing(housing);
                    tripService.addHousingToTrip(idTrip, housing.getId());
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_housing_saved_newhousing), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    public void setListnerToEditTexts(){
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && TextUtils.isEmpty(nameEditText.getText())){
                    nameWrapper.setErrorEnabled(true);
                    nameWrapper.setError(getResources().getString(R.string.error_name_required_create_housing));
                }
            }
        });
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>20){
                    nameWrapper.setErrorEnabled(true);
                    nameWrapper.setError(getResources().getString(R.string.error_length_name_housing_create_housing));
                }
                else {
                    nameWrapper.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        priceEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if(!TextUtils.isEmpty(charSequence.toString())){
                        Integer.valueOf(charSequence.toString());
                    }
                    priceWrapper.setErrorEnabled(false);
                }
                catch (Exception e){
                    priceWrapper.setErrorEnabled(true);
                    priceWrapper.setError(getResources().getString(R.string.msg_field_must_be_integer_housing));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        nbBathRoomEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if(!TextUtils.isEmpty(charSequence.toString())){
                        Integer.valueOf(charSequence.toString());
                    }
                    nbBathRoomWrapper.setErrorEnabled(false);
                }
                catch (Exception e){
                    nbBathRoomWrapper.setErrorEnabled(true);
                    nbBathRoomWrapper.setError(getResources().getString(R.string.msg_field_must_be_integer_housing));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        nbRoomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if(!TextUtils.isEmpty(charSequence.toString())){
                        Integer.valueOf(charSequence.toString());
                    }
                    nbRoomWrapper.setErrorEnabled(false);
                }
                catch (Exception e){
                    nbRoomWrapper.setErrorEnabled(true);
                    nbRoomWrapper.setError(getResources().getString(R.string.msg_field_must_be_integer_housing));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    public Housing checkFields(){
        String name=nameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast toast = Toast.makeText(this.getApplicationContext(), getResources().getString(R.string.error_name_required_create_housing), Toast.LENGTH_LONG);
            toast.show();
            nameEditText.requestFocus(); //donner le focus au champ Nom
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //afficher le clavier
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            return null;
        }
        String nbRoom="";
        String price="";
        String nbBathRoom="";
        try {
            price=priceEditText.getText().toString();
            if(!TextUtils.isEmpty(price)) {
                Integer.valueOf(priceEditText.getText().toString());
            }
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this.getApplicationContext(), getResources().getString(R.string.msg_field_must_be_integer_housing), Toast.LENGTH_LONG);
            toast.show();
            priceEditText.requestFocus(); //donner le focus au champ Nom
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //afficher le clavier
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            return null;
        }
        try {
            nbRoom=nbRoomEditText.getText().toString();
            if(!TextUtils.isEmpty(nbRoom)) {
                Integer.valueOf(nbRoomEditText.getText().toString());
            }
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this.getApplicationContext(), getResources().getString(R.string.msg_field_must_be_integer_housing), Toast.LENGTH_LONG);
            toast.show();
            nbRoomEditText.requestFocus(); //donner le focus au champ Nom
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //afficher le clavier
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            return null;
        }
        try {
            nbBathRoom=nbBathRoomEditText.getText().toString();
            if(!TextUtils.isEmpty(nbBathRoom)) {
                Integer.valueOf(nbBathRoomEditText.getText().toString());
            }
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this.getApplicationContext(), getResources().getString(R.string.msg_field_must_be_integer_housing), Toast.LENGTH_LONG);
            toast.show();
            nbBathRoomEditText.requestFocus(); //donner le focus au champ Nom
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); //afficher le clavier
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            return null;
        }
        String description=descriptionEditText.getText().toString();
        Housing housing=new Housing(name,price,nbRoom,nbBathRoom,description,idTrip);
        return housing;

    }
}

