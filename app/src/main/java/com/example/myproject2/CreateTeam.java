package com.example.myproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class CreateTeam extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-134a1-default-rtdb.firebaseio.com/");
    SharedPreferences sp;
    private  static final String SHARED_PREF_NAME = "mypref";
    private  static final String KEY_PHONE = "phone";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        final EditText teamName = findViewById(R.id.teamName);
        final EditText teamPassword = findViewById(R.id.teamPassword);
        final EditText conTeamPassword = findViewById(R.id.conTeamPassword);
        final Button createBtn = findViewById(R.id.createBtn);
        final TextView joinNowBtn = findViewById(R.id.joinNowBtn);

        sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String phone = sp.getString(KEY_PHONE,null);


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data from EditText into string variables
                final String teamNameTxt = teamName.getText().toString();
                final String teamPasswordTxt = teamPassword.getText().toString();
                final String conTeamPasswordTxt = conTeamPassword.getText().toString();

                //check if user fill all the field
                if(teamNameTxt.isEmpty() ||teamPasswordTxt.isEmpty() || conTeamPasswordTxt.isEmpty()){
                    Toast.makeText(CreateTeam.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                }
                else if (!teamPasswordTxt.equals(conTeamPasswordTxt))
                    Toast.makeText(CreateTeam.this,"Passwords aren't matching",Toast.LENGTH_SHORT).show();
                else{
                    databaseReference.child("teams").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(teamNameTxt)){
                                Toast.makeText(CreateTeam.this,"this team name is taken",Toast.LENGTH_SHORT).show();}
                            else {
                                databaseReference.child("teams").child(teamNameTxt).child("password").setValue(teamPasswordTxt);
                                databaseReference.child("teams").child(teamNameTxt).child("admin").setValue(phone);
                                databaseReference.child("users").child(phone).child("admin").setValue(1);
                                databaseReference.child("users").child(phone).child("team").setValue(teamNameTxt);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("isAdmin", "admin");
                                editor.putString("keyTEAM", teamNameTxt);
                                editor.apply();
                                Toast.makeText(CreateTeam.this,"Team created successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CreateTeam.this,MainActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

        });

        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override

                public void onClick(View view) {
                    startActivity(new Intent(CreateTeam.this,JoinTeam.class));
                }

        });


    }
}