package com.example.myproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://project-134a1-default-rtdb.firebaseio.com/");
    private  static final String SHARED_PREF_NAME = "mypref";
    private  static final String KEY_PHONE = "phone";
    SharedPreferences sp;
    private BatteryLevelReceiver batteryLevelReceiver;
    //String keyTEAM;
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("my_channel_id", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login2);
        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);
        sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        createNotificationChannel();
        batteryLevelReceiver = new BatteryLevelReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, intentFilter);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter your phone number or your password", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phoneTxt)){
                                final String getTeam = snapshot.child(phoneTxt).child("team").getValue(String.class);
                                final String getPassword = snapshot.child(phoneTxt).child("password").getValue(String.class);
                                final Integer isAdmin = snapshot.child(phoneTxt).child("admin").getValue(Integer.class);
                                if (getPassword.equals(passwordTxt)){
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString(KEY_PHONE,phoneTxt);
                                    editor.putString("keyTEAM", getTeam);
                                    editor.apply();



                                    showNotification();




                                    Toast.makeText(Login.this,"Successfully Logged in",Toast.LENGTH_SHORT).show();
                                    if (getTeam == null) {
                                        startActivity(new Intent(Login.this, CreateTeam.class));
                                        finish();
                                    }else{
                                        if(isAdmin != null && isAdmin == 1){
                                            editor.putString("isAdmin", "admin");
                                            editor.apply();
                                        }

                                        startActivity(new Intent(Login.this, MainActivity.class));
                                        finish();
                                    }
                                }
                                else{
                                    Toast.makeText(Login.this,"Wrong password",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(Login.this,"Wrong phone number",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });



    }

    protected void onDestroy() {
        super.onDestroy();

        // Unregister the BatteryLevelReceiver
        unregisterReceiver(batteryLevelReceiver);
    }
    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.ic_baseline_sports_soccer_24)
                .setContentTitle("Welcome back!")
                .setContentText("Logged in successfully")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }


}