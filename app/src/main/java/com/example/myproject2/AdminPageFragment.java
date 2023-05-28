package com.example.myproject2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminPageFragment extends Fragment implements MyAdapter.OnItemClickListener {
    private SharedPreferences sp;
    private String keyTEAM;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_PHONE = "phone";
    private DatabaseReference databaseRef;
    private List<User> items;
    private MyAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_page, container, false);

        sp = requireActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        keyTEAM = sp.getString("keyTEAM", "");
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Get a reference to your Firebase database
        databaseRef = FirebaseDatabase.getInstance().getReference().child("users");

        items = new ArrayList<>();
        adapter = new MyAdapter(requireContext(), items, this);
        recyclerView.setAdapter(adapter);

        // Query the data from Firebase
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String fullName = userSnapshot.child("fullName").getValue(String.class);
                    String overall = userSnapshot.child("overall").getValue(String.class);
                    String team = userSnapshot.child("team").getValue(String.class);

                    if (team != null && team.equals(keyTEAM)) {
                        User user = new User(userSnapshot.getKey(), fullName, overall, team);
                        items.add(user);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur
            }
        });

        return rootView;
    }

    @Override
    public void onItemClick(User user) {
        // Open a dialog or show an EditText to allow user input for changing the item
        showEditDialog(user);
    }

    private void showEditDialog(User user) {
        // Create an EditText and set the current item value
        final EditText editText = new EditText(requireContext());
        editText.setText(user.getOverall());

        // Create a dialog to display the EditText
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Change player's rating");
        builder.setView(editText);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Save the edited item to Firebase
                String newOverall = editText.getText().toString();
                updateUserRating(user.getKey(), newOverall);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateUserRating(String userKey, String newRating) {
        DatabaseReference userRef = databaseRef.child(userKey);
        userRef.child("overall").setValue(newRating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Rating updated successfully
                        Toast.makeText(requireContext(), "Rating updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update rating
                        Toast.makeText(requireContext(), "Failed to update rating", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
