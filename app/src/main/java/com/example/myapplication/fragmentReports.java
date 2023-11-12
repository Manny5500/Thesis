package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class fragmentReports extends Fragment {
    View view;
    FirebaseFirestore db;
    int lightBlueColor, darkBlueColor, whiteColor;
    private Context applicationContext;

    public void onAttach(Context context) {
        super.onAttach(context);
        lightBlueColor = ContextCompat.getColor(context, R.color.colorLightBlue);
        darkBlueColor = ContextCompat.getColor(context, R.color.colorDarkBlue);
        whiteColor = ContextCompat.getColor(context, R.color.white);
        applicationContext = context.getApplicationContext();
        FirebaseApp.initializeApp(requireContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_reports, container, false);
        db = FirebaseFirestore.getInstance();
        String [] barangay = getResources().getStringArray(R.array.barangay);
        db.collection("children").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    ArrayList<Child> childrenList = new ArrayList<>();
                    CategoryBarangay(task, childrenList, barangay);
                    childrenList.clear();
                    GetSexAgeStats(task, childrenList);

                } else {
                    Toast.makeText(requireContext(), "Failed to get children data", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed to get children data", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    private void GetSexAgeStats(Task<QuerySnapshot> task, ArrayList<Child> childrenList){
        int count_Male = 0, count_Female = 0;
        int age0_5, age6_11, age12_23, age24_35, age36_47, age48_59;
        age0_5 = age6_11 = age12_23 = age24_35 = age36_47 = age48_59 = 0;
        for (QueryDocumentSnapshot doc : task.getResult()) {
            Child child = doc.toObject(Child.class);
            child.setId(doc.getId());
            childrenList.add(child);
            Boolean isMale = child.getSex().equals("Male");
            Boolean isFemale = child.getSex().equals("Female");
            Boolean isNormal = child.getStatusdb().contains("Normal");
            if(isMale && !isNormal){
                count_Male++;
            } else if (isFemale && !isNormal) {
                count_Female++;
            }
            String dateString = child.getBirthDate();
            FormUtils formUtils = new FormUtils();
            Date parsedDate = formUtils.parseDate(dateString);
            int monthdiff = 0;
            if (parsedDate != null) {
                monthdiff = formUtils.calculateMonthsDifference(parsedDate);
            }
            if(monthdiff>=0 && monthdiff<=5 && !isNormal){
                age0_5++;
            } else if (monthdiff>=6 && monthdiff<=11 && !isNormal) {
                age6_11++;
            } else if (monthdiff>=12 && monthdiff<=23 && !isNormal) {
                age12_23++;
            } else if (monthdiff>=24 && monthdiff<=35 && !isNormal) {
                age24_35++;
            } else if (monthdiff>=36 && monthdiff<=47 && !isNormal) {
                age36_47++;
            } else if (monthdiff>=48 && monthdiff<=59 && !isNormal) {
                age48_59++;
            }
        }
        int[] colors1 = {lightBlueColor, darkBlueColor, whiteColor};
        PieChart sexChart =  ChartMaker.createPieChart(view, R.id.pieChart, colors1, count_Male, count_Female, childrenList);
        TableLayout sextableLayout = view.findViewById(R.id.sextableLayout);
        String[] sexHeaders = {"Sex", "Total", "Percentage"};
        String[] testPercentage = {
                String.format("%.2f",(double) count_Male/childrenList.size() * 100) + " %",
                String.format("%.2f",(double) count_Female/childrenList.size() * 100) + " %"
        };
        String[][] sexData = {
                {"Male", String.valueOf(count_Male), testPercentage[0]},
                {"Female", String.valueOf(count_Female), testPercentage[1]}
        };
        TableSetter.generateTable(applicationContext, sextableLayout, sexHeaders, sexData);

        ArrayList<BarEntry> entries2 = new ArrayList<>();
        int[] ages_number = {age0_5, age6_11, age12_23, age24_35, age36_47, age48_59};
        for(int i=0; i<6; i++){ entries2.add(new BarEntry(i, ages_number[i]));}

        String[] labels2 = {"0-5", "6-11", "12-23", "24-35", "36-47", "48-59"};
        int[] colors2 = ColorTemplate.PASTEL_COLORS;

        BarChart barChart2 = ChartMaker.createBarChart(view, R.id.barChart2, entries2, labels2, colors2, "Age Group Distribution");

    }
    private void CategoryBarangay(Task<QuerySnapshot> task, ArrayList<Child> childrenList, String [] barangay){
        int [][] record = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
        //double [] malnutrition_rate = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int [] malnutrition_perbarangay = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] index = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
        int count_U, count_SU, count_N, count_W, count_SW, count_S, count_SS, count_O;
        int count_UB, count_SUB, count_NB, count_WB, count_SWB, count_SB, count_SSB, count_OB;
        count_UB = count_SUB = count_NB = count_WB = count_SWB = count_SB = count_SSB = count_OB = 0;
        int count_ALL = 0;
        for (int i = 0; i < index.length; i++) {
            count_U = 0;
            count_SU = 0;
            count_N = 0;
            count_W = 0;
            count_SW = 0;
            count_S = 0;
            count_SS = 0;
            count_O = 0;
            count_ALL = 0;
            for (QueryDocumentSnapshot doc : task.getResult()) {
                Child child = doc.toObject(Child.class);
                child.setId(doc.getId());
                childrenList.add(child);
                Boolean barangaytrue;
                barangaytrue = child.getBarangay().equals(barangay[i]);
                Boolean isUnderWeight = child.getStatusdb().contains("Underweight");
                Boolean isNormal = child.getStatusdb().contains("Normal");
                Boolean isSevereUnderweight = child.getStatusdb().contains("Severe Underweight");
                Boolean isWasted = child.getStatusdb().contains("Wasted");
                Boolean isSevereWasted = child.getStatusdb().contains("Severe Wasted");
                Boolean isStunted = child.getStatusdb().contains("Stunted");
                Boolean isSevereStunted = child.getStatusdb().contains("Severe Stunted");
                Boolean isOverweight = child.getStatusdb().contains("Overweight");
                if(barangaytrue) {
                    if(!isNormal){
                        count_ALL++;
                    }
                    if (isUnderWeight) {
                        count_U++;
                        count_UB++;
                    }
                    if (isNormal) {
                        count_N++;
                        count_NB++;
                    }
                    if (isSevereStunted){
                        count_SS++;
                        count_SSB++;
                    }
                    if (isStunted){
                        count_S++;
                        count_SB++;
                    }
                    if (isOverweight){
                        count_O++;
                        count_OB++;
                    }
                    if (isWasted){
                        count_W++;
                        count_WB++;
                    }
                    if (isSevereWasted){
                        count_SW++;
                        count_SWB++;
                    }
                    if (isSevereUnderweight){
                        count_SU++;
                        count_SUB++;
                    }
                }
                record[i][0] = count_U;
                record[i][1] = count_N;
                record[i][2] = count_SS;
                record[i][3] = count_S;
                record[i][4] = count_O;
                record[i][5] = count_W;
                record[i][6] = count_SW;
                record[i][7] = count_SU;
                malnutrition_perbarangay[i] = count_ALL;
            }
            childrenList.clear();
        }
        RankBarangay(malnutrition_perbarangay, index, barangay);
        int[] numberCategory = {count_UB, count_SUB, count_OB, count_SB, count_SSB, count_WB, count_SWB};
        CategoryTable(numberCategory);

        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] labels = {"U", "SU", "O", "S", "SS", "W", "SW"};
        for(int i = 0; i<labels.length; i++){
            entries.add(new BarEntry(i, numberCategory[i]));
        }
        int[] colors = ColorTemplate.COLORFUL_COLORS;
        BarChart barChart = ChartMaker.createBarChart(view, R.id.barChart, entries, labels, colors, "Malnourished per month");
    }
    private void RankBarangay(int[] malnutrition_perbarangay, int[] index, String[] barangay){
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
        TableLayout tableLayout = view.findViewById(R.id.tableLayout3);
        String[] barangayHeaders = {"Barangay", "Total"};
        String[][] barangayData = {
                {barangay[index[23]], Integer.toString(malnutrition_perbarangay[23])},
                {barangay[index[22]], Integer.toString(malnutrition_perbarangay[22])},
                {barangay[index[21]], Integer.toString(malnutrition_perbarangay[21])},
                {barangay[index[20]], Integer.toString(malnutrition_perbarangay[20])},
                {barangay[index[19]], Integer.toString(malnutrition_perbarangay[19])},
                {barangay[index[18]], Integer.toString(malnutrition_perbarangay[18])}
        };
        TableSetter.generateTable(applicationContext, tableLayout, barangayHeaders, barangayData);
    }
    private void CategoryTable(int[] numberCategory){
        TableLayout tableLayout1 = view.findViewById(R.id.tableLayout);
        String[] category = {"Underweight", "Severe Underweight", "Overweight", "Stunted", "Severe Stunted", "Wasted", "Severe Wasted"};
        String[] categoryHeaders = {"Category", "Total"};
        String[][] categoryData = {
                {category[0], Integer.toString(numberCategory[0])},
                {category[1], Integer.toString(numberCategory[1])},
                {category[2], Integer.toString(numberCategory[2])},
                {category[3], Integer.toString(numberCategory[3])},
                {category[4], Integer.toString(numberCategory[4])},
                {category[5], Integer.toString(numberCategory[5])},
                {category[6], Integer.toString(numberCategory[6])},
        };
        TableSetter.generateTable(applicationContext,tableLayout1, categoryHeaders, categoryData);
    }

}