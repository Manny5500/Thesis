package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Login extends AppCompatActivity {
    TextView admin, donthave, forgotPassword;
    TextInputEditText txtGmail, txtPassword;

    Button login;
    String receivedData;
    FirebaseAuth mAuth;

    ProgressBar progressBar;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FormUtils.redirectToRoleSpecificActivity(currentUser, FirebaseFirestore.getInstance(), this);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        admin = findViewById(R.id.txtRole);
        txtGmail = findViewById(R.id.textGmail);
        txtPassword = findViewById(R.id.textPassword);
        login = findViewById(R.id.btnLogin);
        donthave = findViewById(R.id.txtDontHave);
        forgotPassword = findViewById(R.id.txtForgotPassword);
        progressBar = findViewById(R.id.progressBar);

        Intent intention = getIntent();
        if(intention != null) {
            receivedData = intention.getStringExtra("role");
        }
        //admin, personnel or parent
        if(receivedData.equals("admin")){
            admin.setText("a d m i n");
        }
        if(receivedData.equals("parent")){
            admin.setText("p a r e n t");
        }
        if(receivedData.equals("personnel")){
            admin.setText("p e r s o n n e l");
        }

        admin.setGravity(Gravity.CENTER);
        admin.setAllCaps(true);
        progressBar.setVisibility(View.GONE);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Login.this);
                dialog.setContentView(R.layout.dialog_forgot);
                Button resetPassword = dialog.findViewById(R.id.btnresetPasword);
                EditText forgot = dialog.findViewById(R.id.txtForgotEmail);

                dialog.show();
                resetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean isGmailValid = FormUtils.validateForm_Forgot(forgot.getText().toString(), Login.this);
                        if(isGmailValid){
                            progressBar.setVisibility(View.VISIBLE);
                            mAuth.sendPasswordResetEmail(String.valueOf(forgot.getText().toString()))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Login.this, "Email sent", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });

            }
        });
        donthave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(txtGmail.getText());
                password = String.valueOf(txtPassword.getText());
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User authentication successful, check user role
                                    checkUserRole();
                                } else {
                                    // Authentication failed
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }

    private void checkUserRole() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(userId);

        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String userRole = documentSnapshot.getString("user");
                    // Check the user's role and redirect accordingly
                    if(receivedData.equals("admin") && userRole.equals("admin")) {
                        redirectToActivity(AdminActivity.class);
                        Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    } else if (receivedData.equals("personnel") && userRole.equals("personnel")) {
                        redirectToActivity(PersonnelActivity.class);
                    } else if (receivedData.equals("parent") && userRole.equals("parent")) {
                        Intent intent = new Intent(Login.this, ParentActivity.class);
                        intent.putExtra("email", txtGmail.getText().toString());
                        redirectToActivity(intent);
                    } else {
                        handleInvalidUserRole();
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(Login.this, "You have no privilege to use this app", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void handleInvalidUserRole() {
        FirebaseAuth.getInstance().signOut();
        invalidUserDialog();
    }
    private void redirectToActivity(Class<?> activityClass) {
        Intent intent = new Intent(Login.this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void redirectToActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public  void invalidUserDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);


        builder.setTitle("Invalid User Role")
                .setMessage("Please make sure you select your user role correctly before logging in.");

        builder.setCancelable(true);
        builder.setOnDismissListener(dialog -> {
           redirectToActivity(MainActivity.class);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    };
}