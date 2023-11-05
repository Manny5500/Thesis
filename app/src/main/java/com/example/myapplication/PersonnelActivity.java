package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PersonnelActivity extends AppCompatActivity {

    ImageView personnelProfile, addData, manageData, logOut;
    FirebaseAuth auth;
    FirebaseUser user;

    String email;
    String userid;
    int color_flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            email = user.getEmail();
            userid = user.getUid();
        }
        personnelProfile=findViewById(R.id.btnProfile);
        addData = findViewById(R.id.btnAddData);
        manageData = findViewById(R.id.btnManageData);
        logOut = findViewById(R.id.btnLogout);

        replaceFragment(new ManageData());
        personnelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentPersonnelProfile());
                ButtonColorizer(personnelProfile);
                color_flag = 1;
            }
        });
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fradAddData());
                ButtonColorizer(addData);
                color_flag = 2;
            }

        });
        manageData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ManageData());
                ButtonColorizer(manageData);
                color_flag = 3;
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonColorizer(logOut);
                color_flag = 4;
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutPersonnel,fragment);
        fragmentTransaction.commit();
    }


    private void ButtonColorizer(ImageView button){
        button.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_blue_light));
        if (color_flag ==1) {
            personnelProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag==2){
            addData.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==3){
            manageData.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==4){
            logOut.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }
    }
}