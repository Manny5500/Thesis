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

public class PersonnelActivity extends AppCompatActivity {
    //Button personnelProfile, addData, manageData,syncData, logOut;
    ImageView personnelProfile, addData, manageData, syncData, logOut;

    int color_flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel);

        personnelProfile=findViewById(R.id.btnProfile);
        addData = findViewById(R.id.btnAddData);
        manageData = findViewById(R.id.btnManageData);
        syncData = findViewById(R.id.btnSyncData);
        logOut = findViewById(R.id.btnLogout);

        replaceFragment(new fradAddData());
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
                replaceFragment(new fragmentManageData());
                ButtonColorizer(manageData);
                color_flag = 3;
            }
        });
        syncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentSyncData());
                ButtonColorizer(syncData);
                color_flag = 4;
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonnelActivity.this, MainActivity.class);
                startActivity(intent);
                ButtonColorizer(logOut);
                color_flag = 5;
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
            syncData.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==5){
            logOut.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }
    }
}