package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    ConstraintLayout parentProfile, children, parentLogout;
    ImageView profileImage, cImage, logOutImage;
    String email, userid;
    int color_flag = 0;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);


        email="";
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

        parentProfile = findViewById(R.id.btnParentProfile);
        children = findViewById(R.id.btnChildren);
        parentLogout = findViewById(R.id.btnLogOut);
        profileImage = findViewById(R.id.profileImage);
        cImage = findViewById(R.id.cImage);
        logOutImage = findViewById(R.id.logOutImage);
        ButtonColorizer(cImage);
        color_flag=2;
        replaceFragment(new ParentChildren());
        parentProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ParentProfile());
                // Set the clicked button color to indicate selection
                ButtonColorizer(profileImage);
                color_flag = 1;
            }
        });

        children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ParentChildren());
                ButtonColorizer(cImage);
                color_flag = 2;
            }
        });

        parentLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonColorizer(logOutImage);
                color_flag = 3;
                showYesNoDialog();
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
        if (color_flag ==1) {
            profileImage.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag==2){
            cImage.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==3){
            logOutImage.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }
    }
    private void showYesNoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to log out?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}