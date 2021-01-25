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

// made by Murat Dogan
public class GroupPageActivity extends AppCompatActivity {

    CircleImageView group_image;
    TextView group_name;
    TextView group_desc;
    String groupName, uid;
    Button join_button;
    ActionBar actionBar;
    boolean process = true;

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
        //firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        group_image = findViewById(R.id.group_image);
        group_name = findViewById(R.id.group_name);
        group_desc = findViewById(R.id.group_desc);
        join_button = findViewById(R.id.join_button);

        loadPostInfo();
        checkMember();
        
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroup();
                join_button.setText(getResources().getText(R.string.Joined));
            }
        });
    }

    private void checkMember() {
        final DatabaseReference refMembers = FirebaseDatabase.getInstance().getReference("Members");
        refMembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (process){
                    if (snapshot.child(groupName).hasChild(uid)){
                        join_button.setText(getResources().getText(R.string.Joined));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void joinGroup() {
        final DatabaseReference refMembers = FirebaseDatabase.getInstance().getReference("Members");
        refMembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (process){
                    if (!snapshot.child(groupName).hasChild(uid)){
                        refMembers.child(groupName).child(uid).setValue("Member");
                        process = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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