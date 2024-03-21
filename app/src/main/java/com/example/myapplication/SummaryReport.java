package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class SummaryReport extends AppCompatActivity {

    ConstraintLayout naviData, naviConso, naviSum, naviPdf;
    MaterialAutoCompleteTextView textBarangay, textDate;
    String text_Date, text_Barangay;

    FirebaseFirestore db;

    ArrayList<Child> childrenList;

    int totalCount;
    int loadCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_report);

        naviData = findViewById(R.id.naviData);
        naviConso = findViewById(R.id.naviConso);
        naviSum = findViewById(R.id.naviSum);
        naviPdf = findViewById(R.id.naviPdf);

        textDate = findViewById(R.id.textDate);
        textBarangay = findViewById(R.id.textBarangay);



        db = FirebaseFirestore.getInstance();

        String[] brgyList = getResources().getStringArray(R.array.barangay);
        FormUtils.setAdapter(brgyList, textBarangay, this);
        Populate();
        dateAdapter();
        preSelected();
        textDateEvent();
        textBarangayEvent();
    }

    public void textDateEvent(){
        textDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                text_Barangay = textBarangay.getText().toString();
                text_Date =  textDate.getText().toString();
                Populate();
            }
        });
    }

    public void textBarangayEvent(){
        textBarangay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                text_Barangay = textBarangay.getText().toString();
                text_Date =  textDate.getText().toString();
                Populate();
            }
        });
    }

    public void Populate(){
        db.collection("children")
                .whereEqualTo("monthAdded", text_Date)
                .whereEqualTo("barangay", text_Barangay )
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
                            loadCount++;
                            if(loadCount==2){
                                replaceFragment(new fragment_sr_list());
                            }
                            childrenList = arrayList;
                            naviDataEvent();
                            naviConsoEvent();
                            naviSumEvent();
                            naviPdfEvent();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SummaryReport.this, "Failed to get all users", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void preSelected(){
        text_Date = getDateNowFormatted();
        text_Barangay = "Alipit";
        textDate.setText(getDateNowFormatted(), false);
        textBarangay.setText("Alipit",false);
    }
    public String getDateNowFormatted(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        String formattedDate = currentDate.format(formatter);
        return formattedDate;
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

    private void naviDataEvent(){
        naviData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragment_sr_list());
            }
        });
    }

    private void naviConsoEvent(){
        naviConso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragment_sr_conso());
            }
        });
    }
    private void naviSumEvent(){
        naviSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new fragment_sr_sum());
            }
        });
    }

    private void naviPdfEvent(){
        naviPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SRDPPdf srdpP = new SRDPPdf(childrenList);
                int number[] = srdpP.getTfAges();
                Toast.makeText(SummaryReport.this, ""+number[2], Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutSR,fragment);
        fragmentTransaction.commit();
    }

}