package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class fradAddData extends Fragment {
    View view;

    private ArrayList<User> userlist;
    private String personnelgmail, userid;

    TextInputEditText childFirstName, childMiddleName, childLastName,
            parentFirstName, parentMiddleName, parentLastName,
            gmail, houseNumber, height, weight, bdate, expectedDate;

    MaterialAutoCompleteTextView sexAC, belongAC, sitioAC;
    ArrayList<String> statusdb;
    Button submit;
    private FirebaseFirestore db;
    String barangays, sexchose, belongchose, dateString;
    SimpleDateFormat dateFormat;
    private int bdatevalue, monthdiff;
    private double  height_true_val, weight_true_val;
    String childFirstNameValue, childMiddleNameValue, childLastNameValue,
            parentFirstNameValue, parentMiddleNameValue, parentLastNameValue,
            gmailValue, houseNumberValue, bdateValue, expectedDateValue,
            sexACValue, belongACValue,  heightValue, weightValue;
    
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_frad_add_data, container, false);


        height_true_val = 0;


        personnelgmail = ((PersonnelActivity)getActivity()).email;
        userid = ((PersonnelActivity)getActivity()).userid;
        userlist = new ArrayList<>();

        String[] barangay = getResources().getStringArray(R.array.barangay);
        String[] sex = getResources().getStringArray(R.array.sex);
        String[] belongs = getResources().getStringArray(R.array.yes_or_no);
        String[] sitio;

        db = FirebaseFirestore.getInstance();

        childFirstName = view.findViewById(R.id.textChildfirstName);
        childMiddleName = view.findViewById(R.id.textChildMiddleName);
        childLastName = view.findViewById(R.id.textChildLastName);
        parentFirstName = view.findViewById(R.id.textParentFirstName);
        parentMiddleName = view.findViewById(R.id.textParentMiddleName);
        parentLastName = view.findViewById(R.id.textParentLastName);
        gmail = view.findViewById(R.id.textGmail);
        houseNumber = view.findViewById(R.id.textHouseNumber);
        height = view.findViewById(R.id.textHeight);
        weight = view.findViewById(R.id.textWeight);
        bdate = view.findViewById(R.id.textBdate);
        expectedDate = view.findViewById(R.id.textExpectedDate);
        sexAC = view.findViewById(R.id.textSex);
        belongAC = view.findViewById(R.id.textBelong);
        submit = view.findViewById(R.id.btnSubmit);

        FormUtils.setAdapter(sex, sexAC, requireContext());
        FormUtils.setAdapter(belongs,belongAC, requireContext());
        FormUtils.dateClicked(bdate, requireContext());
        FormUtils.dateClicked(expectedDate, requireContext());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateString = bdate.getText().toString();
                FormUtils formUtils = new FormUtils();
                Date parsedDate = formUtils.parseDate(dateString);


                if (parsedDate != null) {
                    monthdiff = formUtils.calculateMonthsDifference(parsedDate);
                } else {
                    Toast.makeText(requireContext(), "Failed to parse the date", Toast.LENGTH_SHORT).show();
                }

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
                heightValue = height.getText().toString().trim();
                weightValue = weight.getText().toString().trim();

                if(heightValue.isEmpty()||weightValue.isEmpty()){

                }else{
                    height_true_val = Double.parseDouble(heightValue);
                    weight_true_val = Double.parseDouble(weightValue);
                }
                statusdb = FindStatusWFA.CalculateMalnourished(requireContext(), monthdiff, weight_true_val, height_true_val, sexACValue);


                boolean isFormValid = FormUtils.validateForm(childFirstNameValue, childMiddleNameValue, childLastNameValue,
                        parentFirstNameValue, parentMiddleNameValue, parentLastNameValue,
                        gmailValue, houseNumberValue, bdateValue, expectedDateValue,
                        sexACValue, belongACValue, heightValue, weightValue, monthdiff, requireContext());

                if (isFormValid) {
                    getBarangay();

                } else {
                    Toast.makeText(getContext(), "Please fill out all fields and provide valid information", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


    private void AddtoFirestore(String barangayString){
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
        user.put("barangay", barangayString);
        user.put("sitio", "placeholder");
        user.put("sex", sexACValue);
        user.put("expectedDate", expectedDateValue);
        user.put("statusdb", statusdb);
        db.collection("children").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "Form submitted successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed to add user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBarangay(){

        db.collection("users").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AddtoFirestore(String.valueOf(documentSnapshot.getString("barangay")));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed to save changes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}