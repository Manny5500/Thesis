package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    TextView admin, donthave;
    EditText username;
    EditText password;
    Button login;
    String receivedData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        admin = findViewById(R.id.txtRole);
        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        login = findViewById(R.id.btnLogin);
        donthave = findViewById(R.id.txtDontHave);


        Intent intention = getIntent();
        if(intention != null) {
            receivedData = intention.getStringExtra("role");
        }
        //eto yung nasa taas kung admin, personnel or parent
        admin.setText(receivedData);
        admin.setGravity(Gravity.CENTER);
        admin.setAllCaps(true);

        donthave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        //login pinindot
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(receivedData.equals("admin")){
                    //Toast.makeText(Login.this, receivedData,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, AdminActivity.class);
                    startActivity(intent);
                } else if (receivedData.equals("personnel")) {
                    Intent intent = new Intent(Login.this, PersonnelActivity.class);
                    startActivity(intent);
                } else if (receivedData.equals("parent")){
                    Intent intent = new Intent(Login.this, ParentActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);

                }
            }
        });

    }
}