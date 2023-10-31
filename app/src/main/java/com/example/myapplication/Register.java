package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Register extends AppCompatActivity {
    TextInputEditText fname, mname, lname, gmail, password, cpassword, contact;
    MaterialAutoCompleteTextView month, day, year, sex, barangay, userrole;
    String fnameVal, mnameVal, lnameVal,gmailVal, passwordVal, cpasswordVal, contactVal,
    monthVal, dayVal, yearVal, sexVal, barangayVal, bdayfull, motono, userVal;

    Button btnRegister;
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
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        String[] monthCol = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                          "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] barangayCol = getResources().getStringArray(R.array.barangay);
        String[] sexCol = getResources().getStringArray(R.array.sex);
        String[] roleCol = {"admin", "parent", "personnel"};


        fname = findViewById(R.id.textChildfirstName);
        mname = findViewById(R.id.textChildMiddleName);
        lname = findViewById(R.id.textChildLastName);
        gmail = findViewById(R.id.textGmail);
        password = findViewById(R.id.textPassword);
        cpassword = findViewById(R.id.textConfirmPassword);
        month = findViewById(R.id.textMonth);
        day = findViewById(R.id.textDay);
        year = findViewById(R.id.textYear);
        sex = findViewById(R.id.textSex);
        barangay = findViewById(R.id.textBarangay);
        btnRegister = findViewById(R.id.buttonRegister);
        userrole = findViewById(R.id.textUserRole);
        contact = findViewById(R.id.textContact);

        FormUtils.setAdapter(monthCol, month, this);
        FormUtils.setAdapter(sexCol, sex, this);
        FormUtils.setAdapter(barangayCol, barangay, this);
        FormUtils.setAdapter(roleCol, userrole, this);


        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generateNumbers(currentYear-61, currentYear));
        year.setAdapter(yearAdapter);

        ArrayAdapter<Integer> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generateNumbers(1, 31));
        day.setAdapter(dayAdapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FormUtils formUtils = new FormUtils();
                fnameVal = fname.getText().toString().trim();
                mnameVal = mname.getText().toString().trim();
                lnameVal = lname.getText().toString().trim();
                gmailVal = gmail.getText().toString().trim();
                passwordVal = password.getText().toString().trim();
                cpasswordVal = cpassword.getText().toString().trim();
                monthVal = month.getText().toString().trim();
                dayVal = day.getText().toString().trim();
                yearVal = year.getText().toString().trim();
                sexVal = sex.getText().toString().trim();
                barangayVal = barangay.getText().toString().trim();
                motono = formUtils.MotoNo(monthVal);
                userVal = userrole.getText().toString().trim();
                contactVal = contact.getText().toString().trim();
                bdayfull = motono + "/" + dayVal + "/" + yearVal;

                Toast.makeText(Register.this, bdayfull, Toast.LENGTH_SHORT).show();
                boolean isFormValid = formUtils.validateForm_R(fnameVal, mnameVal, lnameVal, gmailVal,
                        bdayfull, sexVal, barangayVal, passwordVal, cpasswordVal, userVal, contactVal, Register.this);

                if(isFormValid){
                    mAuth.createUserWithEmailAndPassword(gmailVal, passwordVal)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //progressBar.setVisibility(View.GONE);
                                        // Sign in success, update UI with the signed-in user's information
                                        //FirebaseUser user = mAuth.getCurrentUser();
                                        /*Toast.makeText(Register.this, "Account Created",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();*/
                                        addUserDataToFirestore();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }


            }
        });
    }

    private void addUserDataToFirestore() {
        // Access the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user object with desired data
        User user = new User(fnameVal, mnameVal, lnameVal, gmailVal, bdayfull, sexVal, barangayVal, userVal, contactVal);

        // Add the user object to the "users" collection in Firestore
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid()) // Use user's UID as document ID
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data added successfully
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(Register.this, "Account Created and Data Added to Firestore",
                                Toast.LENGTH_SHORT).show();
                        if(userVal.equals("admin")){
                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        if(userVal.equals("personnel")){
                            Intent intent = new Intent(getApplicationContext(), PersonnelActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        if(userVal.equals("parent")){
                            Intent intent = new Intent(getApplicationContext(), ParentActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(Register.this, "Failed to add data to Firestore.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private List<Integer> generateNumbers(int start, int end) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = end; i >= start; i--) {
            numbers.add(i);
        }
        return numbers;
    }


}