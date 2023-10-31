package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    TextView admin, donthave;
    TextInputEditText txtGmail, txtPassword;

    Button login;
    String receivedData;
    FirebaseAuth mAuth;


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
                    } else if (receivedData.equals("personnel") && userRole.equals("personnel")) {
                        redirectToActivity(PersonnelActivity.class);
                    } else if (receivedData.equals("parent") && userRole.equals("parent")) {
                        Intent intent = new Intent(Login.this, ParentActivity.class);
                        intent.putExtra("email", txtGmail.getText().toString());
                        redirectToActivity(intent);
                    } else {
                        handleInvalidUserRole();
                    }
                } else {
                    // Document doesn't exist, handle the error or redirect to an appropriate activity.
                }
            }
        });
    }
    private void handleInvalidUserRole() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(Login.this, "Invalid user role. Please choose correctly.", Toast.LENGTH_SHORT).show();
        redirectToActivity(MainActivity.class);
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
}