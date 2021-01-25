package com.emugamestudios.groupup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// made by Ümit Kadiroğlu

public class HomeFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelGroup> list;
    List<String> listMembers;
    AdapterGroups adapterGroups;
    String uid;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.group_rv_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        listMembers = new ArrayList<String>();
        //firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();





        loadJoinedGroups();

        return view;
    }

    private void loadJoinedGroups() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelGroup modelGroup = ds.getValue(ModelGroup.class);

                    list.add(modelGroup);
                    Log.d("myTag", "This is my message" + list);
                    adapterGroups = new AdapterGroups(getActivity(), list);
                    recyclerView.setAdapter(adapterGroups);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onStart() {
        DatabaseReference refMembers = FirebaseDatabase.getInstance().getReference("Members");
        refMembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMembers.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.hasChild(uid)){
                        listMembers.add(ds.getKey());
                    }

                    adapterGroups = new AdapterGroups(getActivity(), list);
                    recyclerView.setAdapter(adapterGroups);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        super.onStart();
    }
}