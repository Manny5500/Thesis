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
import android.widget.ImageView;

public class AdminActivity extends AppCompatActivity {
    ImageButton dashboard, priority, usermanage,  reports;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dashboard = findViewById(R.id.btnDashboards);
        priority = findViewById(R.id.btnPriority);
        usermanage = findViewById(R.id.btnUM);
        logout = findViewById(R.id.btnAdminLogOut);
        reports = findViewById(R.id.btnReports);

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentDashboard());
            }
        });
        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentPriority());
            }
        });
        usermanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentUM());
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentReports());
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutAdmin,fragment);
        fragmentTransaction.commit();
    }
}