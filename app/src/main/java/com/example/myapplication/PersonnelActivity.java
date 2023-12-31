package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
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

public class PersonnelActivity extends AppCompatActivity {

    ConstraintLayout personnelProfile, addData, manageData, logOut;
    ImageView profileImage, addDataImage, manageDataImage, logOutImage;
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
        profileImage = findViewById(R.id.profileImage);
        addDataImage = findViewById(R.id.addDataImage);
        manageDataImage = findViewById(R.id.manageDataImage);
        logOutImage = findViewById(R.id.logOutImage);

        replaceFragment(new ManageData());
        personnelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragmentPersonnelProfile());
                ButtonColorizer(profileImage);
                color_flag = 1;
            }
        });
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fradAddData());
                ButtonColorizer(addDataImage);
                color_flag = 2;
            }

        });
        manageData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ManageData());
                ButtonColorizer(manageDataImage);
                color_flag = 3;
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonColorizer(logOutImage);
                color_flag = 4;
                showYesNoDialog();
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
        button.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        if (color_flag ==1) {
            profileImage.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag==2){
            addDataImage.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==3){
            manageDataImage.setColorFilter(ContextCompat.getColor(this, android.R.color.white));
        }else if(color_flag ==4){
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