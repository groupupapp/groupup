package com.emugamestudios.groupup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    EditText edittext_edit_name, edittext_bio;
    TextInputLayout til_edit_name, til_edit_school, til_edit_department, til_edit_bio;
    AutoCompleteTextView edit_school_spinner, edit_department_spinner;
    CircleImageView current_pp;
    ImageButton button_changephoto;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        current_pp = findViewById(R.id.current_pp);
        button_changephoto = findViewById(R.id.button_changephoto);
        til_edit_bio = findViewById(R.id.til_edit_bio);
        edittext_bio = findViewById(R.id.edittext_bio);
        edittext_edit_name = findViewById(R.id.edittext_edit_name);
        til_edit_name = findViewById(R.id.til_edit_name);
        til_edit_school = findViewById(R.id.til_edit_school);
        til_edit_department = findViewById(R.id.til_edit_department);
        edit_school_spinner = findViewById(R.id.edit_school_spinner);
        edit_department_spinner = findViewById(R.id.edit_department_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.universityList, R.layout.spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.department, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_school_spinner.setAdapter(adapter);
        edit_department_spinner.setAdapter(adapter2);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_activity_edit_profile);
        }

        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    String uni = "" + ds.child("uni").getValue();
                    String department = "" + ds.child("department").getValue();
                    photo = "" + ds.child("photo").getValue();
                    String bio = "" + ds.child("bio").getValue();

                    edittext_edit_name.setText(name);
                    edit_department_spinner.setText(department);
                    edit_school_spinner.setText(uni);
                    edittext_bio.setText(bio);
                    Picasso.get().load(photo).into(current_pp);
                    //Glide.with(EditProfileActivity.this).load(photo).circleCrop().into(current_pp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //create tuşuna basıldığındaki işlem
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {

            String name = edittext_edit_name.getText().toString().trim();
            String school = edit_school_spinner.getText().toString().trim();
            String department = edit_department_spinner.getText().toString().trim();
            String bio = edittext_bio.getText().toString().trim();

            til_edit_name.setError(null);
            til_edit_school.setError(null);
            til_edit_department.setError(null);
            til_edit_bio.setError(null);

            if(name.length()<1){
                til_edit_name.setError(getResources().getString(R.string.name_error));
                edittext_edit_name.setFocusable(true);
            }else if(school.length()<1){
                til_edit_school.setError(getResources().getString(R.string.school_error));
                edit_school_spinner.setFocusable(true);
            }else if(department.length()<1){
                til_edit_department.setError(getResources().getString(R.string.department_error));
                edit_department_spinner.setFocusable(true);
            }else{
                editProfile(name, school, department, bio);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void editProfile(final String name, final String school, final String department, final String bio) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();
        String email = user.getEmail();
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("bio", bio);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("uni", school);
        hashMap.put("department", department);
        hashMap.put("photo", photo);
        //firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        reference.child(uid).setValue(hashMap);
        //close all activities
        finish();
    }
}