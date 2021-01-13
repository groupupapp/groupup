package com.emugamestudios.groupup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupPageActivity extends AppCompatActivity {

    CircleImageView group_image;
    TextView group_name;
    TextView group_desc;
    String groupName;
    Button join_button;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        //actionbar
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        groupName = intent.getStringExtra("groupName");

        group_image = findViewById(R.id.group_image);
        group_name = findViewById(R.id.group_name);
        group_desc = findViewById(R.id.group_desc);
        join_button = findViewById(R.id.join_button);

        loadPostInfo();
    }

    private void loadPostInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        Query query = ref.orderByChild("groupdId").equalTo(groupName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String gTitle = ""+ds.child("groupTitle").getValue();
                    String gDesc = ""+ds.child("groupDescription").getValue();
                    String gPic = ""+ds.child("groupPhoto").getValue();
                    actionBar.setTitle(gTitle);
                    group_name.setText(gTitle);
                    group_desc.setText(gDesc);
                    //image
                    if (gPic.equals("noImage")){
                        group_image.setVisibility(View.GONE);
                    }else{
                        group_image.setVisibility(View.VISIBLE);
                        try {
                            Picasso.get().load(gPic).into(group_image);
                        }catch (Exception e){}
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}