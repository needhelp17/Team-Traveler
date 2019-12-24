package com.example.teamtraveler.presentation.displayTrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.viewpager.widget.ViewPager;
import com.example.teamtraveler.R;
import com.example.teamtraveler.presentation.displayTrip.fragment.TripCompletedFragment;
import com.example.teamtraveler.presentation.displayTrip.fragment.TripInProgressFragment;
import com.example.teamtraveler.presentation.homePage.HomePageActivity;
import com.example.teamtraveler.presentation.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ListTripActivity extends AppCompatActivity {
        private ViewPager viewPager;
        private Fragment fragmentOne,fragmentTwo;
        private FirebaseAuth firebaseAuth;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_trip);
            firebaseAuth=FirebaseAuth.getInstance();
            fragmentOne = TripInProgressFragment.newInstance();
            fragmentTwo = TripCompletedFragment.newInstance();
            Toolbar toolbar = findViewById(R.id.toolbar_list_trip);
            setSupportActionBar(toolbar);

            viewPager = findViewById(R.id.tab_viewpager);
            setupViewPagerAndTabs();
        }

        private void setupViewPagerAndTabs() {
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    if (position == 0) {
                        return fragmentOne;
                    }
                    return fragmentTwo;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    if (position == 0) {
                        return TripInProgressFragment.TAB_NAME;
                    }
                    return TripCompletedFragment.TAB_NAME;
                }


                @Override
                public int getCount() {
                    return 2;
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
