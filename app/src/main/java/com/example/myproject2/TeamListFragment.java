package com.example.myproject2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeamListFragment extends Fragment {

    private SharedPreferences sp;
    private String keyTEAM;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_PHONE = "phone";

    private RecyclerView list1RecyclerView;

    private MyAdapter list1Adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_list, container, false);

        sp = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        keyTEAM = sp.getString("keyTEAM", "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        list1RecyclerView = view.findViewById(R.id.list1RecyclerView);


        list1Adapter = new MyAdapter(requireContext(), new ArrayList<User>());


        list1RecyclerView.setAdapter(list1Adapter);


        list1RecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> teamUsers = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String fullName = userSnapshot.child("fullName").getValue(String.class);
                    String overall = userSnapshot.child("overall").getValue(String.class);
                    String team = userSnapshot.child("team").getValue(String.class);
                    User user = new User(fullName, overall, team);
                    if (team != null && team.equals(keyTEAM))
                        teamUsers.add(user);
                }

                list1Adapter.setUsers(teamUsers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });

        return view;
    }
}

