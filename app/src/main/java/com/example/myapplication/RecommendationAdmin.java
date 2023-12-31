package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RecommendationAdmin extends AppCompatActivity {
    FirebaseFirestore db;
    private List<Child> childList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_admin);

        db = FirebaseFirestore.getInstance();
        String [] barangay = getResources().getStringArray(R.array.barangay);


        db.collection("children").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    ArrayList<Child> childrenList = new ArrayList<>();
                    FeedingProgramBarangay(task,childrenList,barangay);
                    childrenList.clear();

                } else {
                    Toast.makeText(RecommendationAdmin.this, "Failed to get children data", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RecommendationAdmin.this, "Failed to get children data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void generateTable(TableLayout tableLayout, String[] headers, String[][] data, String which) {
        TableRow headerRow = new TableRow(this);

        for (int i = 0; i < headers.length; i++) {
            TextView headerTextView = new TextView(this);
            headerTextView.setText(headers[i]);
            TableSetter.Costumize(headerRow, headerTextView);
            if(i==0){
                headerTextView.setGravity(Gravity.START);
            }else{
                headerTextView.setGravity(Gravity.END);
            }
            TableSetter.setTextSizeAndPaddingForTextViews(18, 16, headerTextView);
            headerRow.addView(headerTextView);
        }

        tableLayout.addView(headerRow);

        // Populate table rows with data
        for (String[] rowData : data) {
            TableRow tableRow = new TableRow(this);

            // Create cells for each column in the row
            for (int i= 0; i<rowData.length; i++){
                TextView cellTextView = new TextView(this);
                cellTextView.setText(rowData[i]);
                TableSetter.Costumize(tableRow, cellTextView);
                if(i==0){
                    cellTextView.setGravity(Gravity.START);
                }else{
                    cellTextView.setGravity(Gravity.END);
                }
                TableSetter.setTextSizeAndPaddingForTextViews(18, 16, cellTextView);
                tableRow.addView(cellTextView);
                String maybe = rowData[0];
                if(i==rowData.length-1 && which.equals("barangay")){
                    if(Integer.parseInt(rowData[1])>0) {
                        cellTextView.setTextColor(Color.parseColor("#0000FF"));
                        FireStoreUtility.getBarangayStatus(maybe, cellTextView, db);
                        cellTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog dialog = new Dialog(RecommendationAdmin.this);
                                dialog.setContentView(R.layout.dialog_assign);
                                FireStoreUtility.getBarangayDetails(maybe, dialog, db,RecommendationAdmin.this);
                                dialog.show();
                            }
                        });
                    } else{
                        cellTextView.setTextColor(Color.parseColor("#FF0000"));
                    }
                }

                if(i==rowData.length-1 && which.equals("gulayan")){

                    if(Integer.parseInt(rowData[1])>0) {
                        cellTextView.setTextColor(Color.parseColor("#0000FF"));
                        FireStoreUtility.getGulayanStatus(rowData[0], cellTextView, db);

                        cellTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog dialog = new Dialog(RecommendationAdmin.this);
                                dialog.setContentView(R.layout.dialog_assign);
                                FireStoreUtility.getGulayanDetails(rowData[0], dialog, rowData[0],db,RecommendationAdmin.this);
                                dialog.show();
                            }
                        });

                    } else{
                        cellTextView.setTextColor(Color.parseColor("#FF0000"));
                    }
                }
            }
            tableLayout.addView(tableRow);
        }
    }
    public void FeedingProgramBarangay(Task<QuerySnapshot> task, ArrayList<Child> childrenList, String [] barangay){
        int [] feeding_barangay = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] index = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
        int count_FEEDING;
        int count_GSB, count_MBL, count_M, count_L;
        count_GSB = count_MBL = count_M = count_L = 0;
        for (int i = 0; i < index.length; i++) {
            count_FEEDING = 0;
            for (QueryDocumentSnapshot doc : task.getResult()) {
                Child child = doc.toObject(Child.class);
                child.setId(doc.getId());
                childrenList.add(child);
                Boolean barangaytrue;
                barangaytrue = child.getBarangay().equals(barangay[i]);
                Boolean isUnderWeight = child.getStatusdb().contains("Underweight");
                Boolean isWasted = child.getStatusdb().contains("Wasted");
                Boolean isStunted = child.getStatusdb().contains("Stunted");
                Boolean isNormal = child.getStatusdb().contains("Normal");
                Boolean isLowestIncome = child.getMonthlyIncome().equals("Less than 9,100");
                Boolean isLowerIncome = child.getMonthlyIncome().equals("9,100 to 18,200");
                Boolean isLowIncome = child.getMonthlyIncome().equals("18,200 to 36,400");
                String bdate = child.getBirthDate();
                FormUtils formUtils = new FormUtils();
                Date parsedDate = formUtils.parseDate(bdate);
                int monthdiff = 0;
                if (parsedDate != null) {
                    monthdiff = formUtils.calculateMonthsDifference(parsedDate);
                }
                if(barangaytrue) {
                    if((isUnderWeight||isStunted||isWasted) && (monthdiff>23)){
                        count_FEEDING++;
                    }
                    if((!isNormal) || (isLowerIncome||isLowIncome||isLowestIncome)){
                        count_GSB++;
                    }
                    if((isNormal) && (isLowerIncome||isLowIncome||isLowestIncome)){
                        count_L++;
                    }
                    if(!isNormal){
                        count_M++;
                    }
                    if((!isNormal) && (isLowerIncome||isLowIncome||isLowestIncome)){
                        count_MBL++;
                    }
                }
                feeding_barangay[i] = count_FEEDING;
            }
            childrenList.clear();
        }
        RankBarangay(feeding_barangay, index, barangay, task.getResult().size());
        String[] gulayanHeaders = {"Beneficiaries","Total", "Action"};
        String[][] gulayanData = {{"Malnourished but Low Income", Integer.toString(count_MBL),"Set"},
                {"Malnourished", Integer.toString(count_M),"Set"},
                {"Low Income", Integer.toString(count_L),"Set"},
                {"All Beneficiaries", Integer.toString(count_GSB), "Set All"}};
        TableLayout tableLayout = findViewById(R.id.GulayanTable);
        generateTable(tableLayout, gulayanHeaders, gulayanData, "gulayan" );
    }

    private void RankBarangay(int[] malnutrition_perbarangay, int[] index, String[] barangay, int totalcase){
        for (int i = 0; i < malnutrition_perbarangay.length - 1; i++) {
            for (int j = i + 1; j < malnutrition_perbarangay.length; j++) {
                if (malnutrition_perbarangay[i] > malnutrition_perbarangay[j]) {
                    // Swap malnutrition_rate[i] and malnutrition_rate[j]
                    int tempRate = malnutrition_perbarangay[i];
                    malnutrition_perbarangay[i] = malnutrition_perbarangay[j];
                    malnutrition_perbarangay[j] = tempRate;

                    // Swap index[i] and index[j]
                    int tempIndex = index[i];
                    index[i] = index[j];
                    index[j] = tempIndex;
                }
            }
        }
        TableLayout tableLayout = findViewById(R.id.feedingProgramTable);
        String[] barangayHeaders = {"Barangay", "Total", "Rate", "Action"};
        int[] topNumbers = {malnutrition_perbarangay[23], malnutrition_perbarangay[22], malnutrition_perbarangay[21]};
        String[][] barangayData = {
                {barangay[index[23]], Integer.toString(topNumbers[0]), String.format("%.3f",(double) topNumbers[0]/totalcase) + " %", "Set"},
                {barangay[index[22]], Integer.toString(topNumbers[1]), String.format("%.3f", (double) topNumbers[1]/totalcase) + " %", "Set"},
                {barangay[index[21]], Integer.toString(topNumbers[2]), String.format("%.3f",(double) topNumbers[2]/totalcase) + " %", "Set"}
        };
        generateTable(tableLayout, barangayHeaders, barangayData, "barangay");
    }
}