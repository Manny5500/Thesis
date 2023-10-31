package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Date;

public class PriorityClicked extends AppCompatActivity {

    private static final int REQUEST_PHONE_CALL = 1;
    String phoneNumber = "123456789";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_clicked);

        Button callButton = findViewById(R.id.btnCallDynamics);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PriorityClicked.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PriorityClicked.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    // Permission has already been granted
                    makePhoneCall(phoneNumber);
                }
            }
        });

        TextView textName, textAge, textSex, textWeight,
                textHeight, textStatus;

        TextView textNameLabel, textAgeLabel, textGenderLabel,
                textWeightLabel, textHeightLabel, textStatusLabel;

        textName = findViewById(R.id.textNameDynamics);
        textAge = findViewById(R.id.textAgeDynamics);
        textSex = findViewById(R.id.textMaleDynamics);
        textWeight = findViewById(R.id.textWeightDynamics);
        textHeight = findViewById(R.id.textHeightDynamics);
        textStatus = findViewById(R.id.textStatusDynamics);

        textNameLabel = findViewById(R.id.labelPCName);
        textAgeLabel = findViewById(R.id.labelAge);
        textGenderLabel = findViewById(R.id.labelGenderDynamics);
        textWeightLabel = findViewById(R.id.labelWeightDynamics);
        textHeightLabel = findViewById(R.id.labelHeightDynamics);
        textStatusLabel = findViewById(R.id.labelStatusDynamics);

        textName.setText(App.child.getChildFirstName()+" "+App.child.getChildLastName());
        textSex.setText(App.child.getSex());
        textWeight.setText(App.child.getWeight() + " kg");
        textHeight.setText(App.child.getHeight() + " cm");
        String statusmaker = "";
        for(String status : App.child.getStatusdb()){
            statusmaker = statusmaker + status + "\n";
        }
        textStatus.setText(statusmaker);


        FormUtils formUtils = new FormUtils();
        Date parsedDate = formUtils.parseDate(App.child.getBirthDate());

        int monthdiff = 0;

        if (parsedDate != null) {
            monthdiff = formUtils.calculateMonthsDifference(parsedDate);
            textAge.setText(String.valueOf(monthdiff)+ " months");

        } else {
            Toast.makeText(this, "Failed to parse the date", Toast.LENGTH_SHORT).show();
        }

        setTextSize(textAge,textStatus, textSex, textName, textWeight, textHeight);
        setTextSize(textAgeLabel, textStatusLabel, textGenderLabel, textNameLabel, textWeightLabel, textHeightLabel);


    }

    private void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(phoneNumber);
            } else {
                // Permission denied, inform the user and handle accordingly
                Toast.makeText(this, "Please allow the phone permission in the settings", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setTextSize(TextView... textViews){
        for(TextView textView: textViews){
            textView.setTextSize(18);
        }
    }
}
