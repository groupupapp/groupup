package com.emugamestudios.groupup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {
    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    BottomNavigationView navigation;
    String yourname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //bottom navigation
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(selectedListener);

        //start home fragment default
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.content, fragment1, "");
        fragmentTransaction1.commit();
    }

    //bottom navigation transitions
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //item clicks
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            //home fragment
                            ActionBar actionBar1 = getSupportActionBar();
                            actionBar1.setTitle(getResources().getString(R.string.app_name));
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction1.replace(R.id.content, fragment1, "");
                            fragmentTransaction1.commit();
                            return true;
                        case R.id.nav_search:
                            //search fragment
                            ActionBar actionBar2 = getSupportActionBar();
                            actionBar2.setTitle(getResources().getString(R.string.explore));
                            SearchFragment fragment2 = new SearchFragment();
                            FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction2.replace(R.id.content, fragment2, "");
                            fragmentTransaction2.commit();
                            return true;
                        case R.id.nav_add:
                            //add activity
                            Intent intent = new Intent(DashboardActivity.this, AddActivity.class);
                            startActivity(intent);
                            return false;
                        case R.id.nav_notifications:
                            //add fragment
                            ActionBar actionBar4 = getSupportActionBar();
                            actionBar4.setTitle(getResources().getString(R.string.notifications));
                            NotificationsFragment fragment4 = new NotificationsFragment();
                            FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction4.replace(R.id.content, fragment4, "");
                            fragmentTransaction4.commit();
                            return true;
                        case R.id.nav_profile:
                            //profile fragment
                            ActionBar actionBar5 = getSupportActionBar();
                            actionBar5.setTitle(yourname);
                            ProfileFragment fragment5 = new ProfileFragment();
                            FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction5.replace(R.id.content, fragment5, "");
                            fragmentTransaction5.commit();
                            return true;
                    }
                    return false;
                }
            };

    //check user and get name
    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        yourname = "" + ds.child("name").getValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        }
    }

    //check user on start
    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }
}