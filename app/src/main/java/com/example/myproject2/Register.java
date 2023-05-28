package com.example.myproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-134a1-default-rtdb.firebaseio.com/");
    Button openDialog;
    TextView infoTv;
    SharedPreferences sp;
    private  static final String SHARED_PREF_NAME = "mypref";
    private  static final String KEY_PHONE = "phone";
    private  static final String KEY_TEAM = "team";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        openDialog = findViewById(R.id.open_dialog);
        infoTv = findViewById(R.id.info_tv);
        sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

    }

    //Function to display the custom dialog.
    void showCustomDialog() {
        final Dialog dialog = new Dialog(Register.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);

        final EditText nameEt = dialog.findViewById(R.id.fullName);
        final EditText phoneEt = dialog.findViewById(R.id.phone);
        final EditText passwordEt = dialog.findViewById(R.id.password);
        final EditText conPasswordEt = dialog.findViewById(R.id.conPassword);
        Button registerButton = dialog.findViewById(R.id.registerBtn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullNameTxt = nameEt.getText().toString();
                String phoneTxt = phoneEt.getText().toString();
                String passwordTxt = passwordEt.getText().toString();
                String conPasswordTxt = conPasswordEt.getText().toString();

                if (fullNameTxt.isEmpty() || passwordTxt.isEmpty() || phoneTxt.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(conPasswordTxt)) {
                    Toast.makeText(Register.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phoneTxt)) {
                                Toast.makeText(Register.this, "Phone is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("users").child(phoneTxt).child("fullName").setValue(fullNameTxt);
                                databaseReference.child("users").child(phoneTxt).child("password").setValue(passwordTxt);
                                databaseReference.child("users").child(phoneTxt).child("overall").setValue("60");
                                //databaseReference.child("users").child(phoneTxt).child("team").setValue(null);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(KEY_PHONE, phoneTxt);
                                editor.apply();

                                Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
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

        TextView loginNowBtn = dialog.findViewById(R.id.loginNowBtn);
        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        dialog.show();
    }








}
