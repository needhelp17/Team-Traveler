package com.example.teamtraveler.presentation.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.teamtraveler.R;
import com.example.teamtraveler.presentation.createNewTrip.NewTripActivity;
import com.example.teamtraveler.presentation.displayTrip.ListTripActivity;
import com.example.teamtraveler.presentation.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePageActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();

        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Button newTrip = findViewById(R.id.button_create_new_trip);
        final Button btnListTrip = findViewById(R.id.button_list_trip);
        newTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), NewTripActivity.class);
                    startActivity(intent); }
            });
        btnListTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListTripActivity.class);
                startActivity(intent); }
        });
    }

    @Override
    public void onBackPressed(){
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            finishAffinity();// a revoir
        }
        else{
            super.onBackPressed();
        }
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
            this.recreate();
        }

        return super.onOptionsItemSelected(item);
    }

}
