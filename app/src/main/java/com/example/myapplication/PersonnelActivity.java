package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PersonnelActivity extends AppCompatActivity {
    //Button personnelProfile, addData, manageData,syncData, logOut;
    ImageButton personnelProfile, addData, manageData, syncData, logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel);

        personnelProfile=findViewById(R.id.btnProfile);
        addData = findViewById(R.id.btnAddData);
        manageData = findViewById(R.id.btnManageData);
        syncData = findViewById(R.id.btnSyncData);
        logOut = findViewById(R.id.btnLogout);
        personnelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentPersonnelProfile());
            }
        });
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fradAddData());
            }
        });
        manageData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentManageData());
            }
        });
        syncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentSyncData());
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonnelActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutPersonnel,fragment);
        fragmentTransaction.commit();
    }
}