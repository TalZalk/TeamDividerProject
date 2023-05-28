package com.example.myproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinTeam extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-134a1-default-rtdb.firebaseio.com/");
    SharedPreferences sp;
    private  static final String SHARED_PREF_NAME = "mypref";
    private  static final String KEY_PHONE = "phone";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);
        final EditText teamName = findViewById(R.id.teamName);
        final EditText teamPassword = findViewById(R.id.teamPassword);
        final Button joinBtn = findViewById(R.id.joinBtn);
        final TextView createNowBtn = findViewById(R.id.createNowBtn);

        sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String phone = sp.getString(KEY_PHONE,null);



        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String teamNameTxt =teamName.getText().toString();
                final String teamPasswordTxt = teamPassword.getText().toString();

                if (teamNameTxt.isEmpty() || teamPasswordTxt.isEmpty()) {
                    Toast.makeText(JoinTeam.this, "Please enter your team name and password", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("teams").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(teamNameTxt)){
                                final String getTeamPassword = snapshot.child(teamNameTxt).child("password").getValue(String.class);
                                if (getTeamPassword.equals(teamPasswordTxt)){
                                    String groupAdminPhone = snapshot.child(teamNameTxt).child("phone").getValue(String.class);

                                    //sendNotificationToUser(groupAdminPhone, "new user just joined your team");
                                    databaseReference.child("users").child(phone).child("team").setValue(teamNameTxt);
                                    Toast.makeText(JoinTeam.this,"Successfully joined",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(JoinTeam.this,MainActivity.class));
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("keyTEAM", teamNameTxt);
                                    editor.apply();
                                    finish();
                                }
                                else{
                                    Toast.makeText(JoinTeam.this,"Wrong password",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(JoinTeam.this,"Team doesn't exist",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        createNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                startActivity(new Intent(JoinTeam.this,CreateTeam.class));
            }

        });

    }




}