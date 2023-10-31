package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ParentActivity extends AppCompatActivity {
    ImageView parentNotify, parentProfile, children, parentLogout;
    String email;
    int color_flag = 0;

    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        //Intent intent = getIntent();

        //email = intent.getStringExtra("email");
        email="";
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            email = user.getEmail();
        }
        //Toast.makeText(this, email, Toast.LENGTH_SHORT).show();

        parentProfile = findViewById(R.id.btnParentProfile);
        children = findViewById(R.id.btnChildren);
        parentLogout = findViewById(R.id.btnLogOut);
        parentNotify = findViewById(R.id.btnParentNotify);
        replaceFragment(new ParentChildren());
        parentProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ParentProfile());
                // Set the clicked button color to indicate selection
                ButtonColorizer(parentProfile);
                color_flag = 1;
            }
        });

        children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ParentChildren());
                ButtonColorizer(children);
                color_flag = 2;
            }
        });

        parentNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new parent_notification());
                ButtonColorizer(parentNotify);
                color_flag = 3;
            }
        });
        parentLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonColorizer(parentLogout);
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
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    private void ButtonColorizer(ImageView button){
        button.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_blue_light));
        if (color_flag ==1) {
            parentProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag==2){
            children.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==3){
            parentNotify.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==4){
            parentLogout.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }
    }
}