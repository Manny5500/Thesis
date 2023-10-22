package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditChild extends AppCompatActivity {


    TextInputEditText childFirstName, childMiddleName, childLastName,
            parentFirstName, parentMiddleName, parentLastName,
            gmail, houseNumber, height, weight, bdate, expectedDate;
    MaterialAutoCompleteTextView barangayAC, sexAC, belongAC, sitioAC;
    Button edit, remove;
    private FirebaseFirestore db;
    String barangays, sexchose, belongchose, dateString;
    SimpleDateFormat dateFormat;
    private int height_true_val, weight_true_val, bdatevalue, monthdiff;

    String childFirstNameValue, childMiddleNameValue, childLastNameValue,
            parentFirstNameValue, parentMiddleNameValue, parentLastNameValue,
            gmailValue, houseNumberValue, bdateValue, expectedDateValue,
            sexACValue, belongACValue, barangayACValue, heightValue, weightValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);


        String[] barangay = getResources().getStringArray(R.array.barangay);
        String[] sex = getResources().getStringArray(R.array.sex);
        String[] belongs = getResources().getStringArray(R.array.yes_or_no);
        String[] sitio;

        db = FirebaseFirestore.getInstance();

        childFirstName = findViewById(R.id.textChildfirstName);
        childMiddleName = findViewById(R.id.textChildMiddleName);
        childLastName = findViewById(R.id.textChildLastName);
        parentFirstName = findViewById(R.id.textParentFirstName);
        parentMiddleName = findViewById(R.id.textParentMiddleName);
        parentLastName = findViewById(R.id.textParentLastName);
        gmail = findViewById(R.id.textGmail);
        houseNumber = findViewById(R.id.textHouseNumber);
        height = findViewById(R.id.textHeight);
        weight = findViewById(R.id.textWeight);
        bdate = findViewById(R.id.textBdate);
        expectedDate = findViewById(R.id.textExpectedDate);
        sexAC = findViewById(R.id.textSex);
        belongAC = findViewById(R.id.textBelong);
        barangayAC = findViewById(R.id.textBarangay);
        edit = findViewById(R.id.btnEdit);
        remove = findViewById(R.id.btnDelete);

        gmail.setText(App.child.getGmail());
        houseNumber.setText(App.child.getHouseNumber());
        height.setText(String.valueOf(App.child.getHeight()));
        weight.setText(String.valueOf(App.child.getWeight()));

        childFirstName.setText(App.child.getChildFirstName());
        childMiddleName.setText(App.child.getChildMiddleName());
        childLastName.setText(App.child.getChildLastName());
        parentFirstName.setText(App.child.getParentFirstName());
        parentMiddleName.setText(App.child.getParentMiddleName());
        parentLastName.setText(App.child.getParentLastName());

        barangayAC.setText(App.child.getBarangay());
        sexAC.setText(App.child.getSex());
        belongAC.setText(App.child.getBelongtoIP());
        bdate.setText(App.child.getBirthDate());
        expectedDate.setText(App.child.getExpectedDate());

        FormUtils.setAdapter(sex, sexAC, this);
        FormUtils.setAdapter(belongs,belongAC,this);
        FormUtils.setAdapter(barangay, barangayAC, this);
        FormUtils.dateClicked(bdate,this);
        FormUtils.dateClicked(expectedDate, this);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childFirstNameValue = childFirstName.getText().toString().trim();
                childMiddleNameValue = childMiddleName.getText().toString().trim();
                childLastNameValue = childLastName.getText().toString().trim();
                parentFirstNameValue = parentFirstName.getText().toString().trim();
                parentMiddleNameValue = parentMiddleName.getText().toString().trim();
                parentLastNameValue = parentLastName.getText().toString().trim();
                gmailValue = gmail.getText().toString().trim();
                houseNumberValue = houseNumber.getText().toString().trim();
                bdateValue = bdate.getText().toString().trim();
                expectedDateValue = expectedDate.getText().toString().trim();
                sexACValue = sexAC.getText().toString().trim();
                belongACValue = belongAC.getText().toString().trim();
                barangayACValue = barangayAC.getText().toString().trim();
                heightValue = height.getText().toString().trim();
                weightValue = weight.getText().toString().trim();

                height_true_val = Integer.parseInt(heightValue);
                weight_true_val = Integer.parseInt(weightValue);

                boolean isFormValid = FormUtils.validateForm(childFirstNameValue, childMiddleNameValue, childLastNameValue,
                        parentFirstNameValue, parentMiddleNameValue, parentLastNameValue,
                        gmailValue, houseNumberValue, bdateValue, expectedDateValue,
                        sexACValue, belongACValue, barangayACValue, heightValue, weightValue,EditChild.this);

                if (isFormValid) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("childFirstName", childFirstNameValue);
                    user.put("childMiddleName", childMiddleNameValue);
                    user.put("childLastName", childLastNameValue);
                    user.put("parentFirstName", parentFirstNameValue);
                    user.put("parentMiddleName", parentMiddleNameValue);
                    user.put("parentLastName", parentLastNameValue);
                    user.put("gmail", gmailValue);
                    user.put("houseNumber", houseNumberValue);
                    user.put("height", height_true_val);
                    user.put("weight", weight_true_val);
                    user.put("birthDate", bdateValue);
                    user.put("belongtoIP", belongACValue);
                    user.put("barangay", barangayACValue);
                    user.put("sitio", "placeholder");
                    user.put("sex", sexACValue);
                    user.put("expectedDate", expectedDateValue);
                    db.collection("children").document(App.child.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditChild.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditChild.this, "Failed to save changes", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(EditChild.this, "Please fill out all fields and provide valid information", Toast.LENGTH_SHORT).show();
                }
            }
        });


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("children").document(App.child.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditChild.this, "User deleted sucessfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditChild.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}