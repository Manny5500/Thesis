package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Prevailance_Reports extends AppCompatActivity {
    MaterialAutoCompleteTextView textType, textDate;
    TextInputLayout textTypeLayout;

    View up_View;
    TextView textThisMonth,  textTypeTitle, textDataThisMonth,
            textIncreased, textDataIncreased;
    FloatingActionButton pdfMaker;
    FirebaseFirestore db;
    RecyclerView barangayRecycler;
    private ChildAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevailance_reports);
        db = FirebaseFirestore.getInstance();


        textType = findViewById(R.id.textType);
        textDate = findViewById(R.id.textDate);
        up_View = findViewById(R.id.up_View);
        textThisMonth = findViewById(R.id.textThisMonth);
        textIncreased = findViewById(R.id.textIncreased);
        pdfMaker = findViewById(R.id.floatingActionButton);
        textTypeLayout = findViewById(R.id.textTypeLayout);
        textTypeTitle = findViewById(R.id.textTypeTitle);
        textDataThisMonth = findViewById(R.id.textDataThisMonth);
        textDataIncreased = findViewById(R.id.textDataIncreased);

        String[] mType = {"Underweight", "Overweight", "Stunting", "Wasting"};
        FormUtils.setAdapter(mType, textType, this);
        dateAdapter();
        preSelected();
        barangayRecycler = findViewById(R.id.barangayRecycler);
        textType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                String[] status_array = {"",""};
                if(selectedType.equals("Stunting")){
                    themeSetter("Stunting");
                    status_array[0] = "Stunted";
                    status_array[1] = "Severe Stunted";
                } else if (selectedType.equals("Overweight")) {
                    status_array[0] = "Overweight";
                    status_array[1] = "Obese";
                    themeSetter("Overweight");
                } else if (selectedType.equals("Underweight")) {
                    status_array[0] = "Underweight";
                    status_array[1] = "Severe Stunted";
                    themeSetter("Underweight");
                } else if (selectedType.equals("Wasting")){
                    status_array[0] = "Wasted";
                    status_array[1] = "Severe Wasted";
                    themeSetter("Wasting");
                }
                Populate(status_array);
            }
        });

    }
    public void themeSetter(String choice){
        int colorString;
        if(choice.equals("Stunting")){
            colorString = Color.parseColor("#77DD77");
            UIChanger(colorString, choice);
        } else if (choice.equals("Underweight")) {
            colorString = Color.parseColor("#51ADE5");
            UIChanger(colorString, choice);
        } else if (choice.equals("Overweight")) {
            colorString = Color.parseColor("#FF6961");
            UIChanger(colorString, choice);
        } else if (choice.equals("Wasting")) {
            colorString = Color.parseColor("#FFB347");
            UIChanger(colorString, choice);
        }
    }
    public void statusBarChanger(int color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor((color));
        }
    }
    public void UIChanger(int colorString, String type){
        ColorStateList colorStateList = ColorStateList.valueOf(colorString);
        up_View.setBackgroundTintList(colorStateList);
        textThisMonth.setTextColor(colorStateList);
        textIncreased.setTextColor(colorStateList);
        pdfMaker.setBackgroundTintList(colorStateList);
        statusBarChanger(colorString);
        textChanger(type);
        if(type.equals("Stunting")){
            textChanger("Stunted and Severe Stunted");
        } else if (type.equals("Underweight")) {
            textChanger("Underweight and Severe Underweight");
        } else if (type.equals("Overweight")) {
            textChanger("Overweight and Severe Overweight");
        } else if (type.equals("Wasting")) {
            textChanger("Wasted and Severe Wasted");
        }
    }
    public void textChanger(String typeTitle){
        textTypeTitle.setText(typeTitle);
    }
    public void dateAdapter(){
        LocalDate currentDate = LocalDate.now();
        YearMonth targetMonth = YearMonth.of(2023,6);
        ArrayList<String> filteredMonths  = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        for (YearMonth month = targetMonth;
             !month.isAfter(YearMonth.from(currentDate));
             month = month.plusMonths(1)) {
            filteredMonths.add(month.atDay(1).format(formatter));
        }
        Collections.reverse(filteredMonths);
        ArrayAdapter<String> adapter = new
                ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,filteredMonths);
        textDate.setAdapter(adapter);
    }
    public void preSelected(){
        String[] status_array = {"Underweight", "Severe Underweight"};
        textDate.setText(getDateNowFormatted(), false);
        textType.setText("Underweight",false);
        int preColor = Color.parseColor("#51ADE5");
        UIChanger(preColor,"Underweight");
        statusBarChanger(preColor);
        Populate(status_array);
    }
    public String getDateNowFormatted(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        String formattedDate = currentDate.format(formatter);
        return formattedDate;
    }

    public void Populate(String[] status_array){
        db.collection("children")
                .whereArrayContainsAny("statusdb", Arrays.asList(status_array))
                .whereEqualTo("expectedDate", "23/11/2023")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Child> arrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        Child child = doc.toObject(Child.class);
                        child.setId(doc.getId());
                        arrayList.add(child);
                    }
                    userAdapter = new ChildAdapter(Prevailance_Reports.this, arrayList);
                    barangayRecycler.setAdapter(userAdapter);
                    userAdapter.setOnItemClickListener(new ChildAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(Child child) {

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Prevailance_Reports.this, "Failed to get all users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}