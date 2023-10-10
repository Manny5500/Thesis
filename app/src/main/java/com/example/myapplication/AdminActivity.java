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

public class AdminActivity extends AppCompatActivity {
    ImageView dashboard, priority, usermanage,  reports, logout;

    int color_flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dashboard = findViewById(R.id.btnDashboards);
        priority = findViewById(R.id.btnPriority);
        usermanage = findViewById(R.id.btnUM);
        logout = findViewById(R.id.btnAdminLogOut);
        reports = findViewById(R.id.btnReports);

        replaceFragment(new fragmentDashboard());
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentDashboard());
                ButtonColorizer(dashboard);
                color_flag = 1;
            }
        });
        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentPriority());
                ButtonColorizer(priority);
                color_flag = 2;
            }
        });
        usermanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentUM());
                ButtonColorizer(usermanage);
                color_flag = 3;
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentReports());
                ButtonColorizer(reports);
                color_flag = 5;
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
                ButtonColorizer(logout);
                color_flag = 4;
            }
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutAdmin,fragment);
        fragmentTransaction.commit();
    }

    private void ButtonColorizer(ImageView button){
        button.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_blue_light));
        if (color_flag ==1) {
            dashboard.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag==2){
            priority.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==3){
            usermanage.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==4){
            logout.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==5){
            reports.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }
    }
}