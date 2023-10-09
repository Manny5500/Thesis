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

        // Get the name from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("name")) {
            String itemName = intent.getStringExtra("name");
            TextView textName = findViewById(R.id.textNameDynamics);
            phoneNumber = intent.getStringExtra("pnumber");
            //Toast.makeText(this,itemName,Toast.LENGTH_SHORT).show();
            textName.setText(itemName);
        }
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
}
