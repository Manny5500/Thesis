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
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_reports, container, false);
        db = FirebaseFirestore.getInstance();


        String [] barangay = getResources().getStringArray(R.array.barangay);
        db.collection("children").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Child> childrenList = new ArrayList<>();
                    int [][] record = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},
                            {0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
                    double [] malnutrition_rate = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
                    int [] malnutrition_perbarangay = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
                    int[] index = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
                    int count_U, count_SU, count_N, count_W, count_SW, count_S, count_SS, count_O;
                    int count_UB, count_SUB, count_NB, count_WB, count_SWB, count_SB, count_SSB, count_OB;
                    count_UB = count_SUB = count_NB = count_WB = count_SWB = count_SB = count_SSB = count_OB = 0;
                    int count_ALL = 0;
                    for (int i = 0; i < 24; i++) {
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

                                if (isUnderWeight||isSevereUnderweight||isStunted||isSevereStunted||isWasted||isSevereWasted||isOverweight){
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

                    //---- Getting the sex  and age statistics ----//
                    childrenList.clear();
                    int count_Male = 0;
                    int count_Female = 0;
                    int age0_5, age6_11, age12_23, age24_35, age36_47, age48_59;
                    age0_5 = age6_11 = age12_23 = age24_35 = age36_47 = age48_59 = 0;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Child child = doc.toObject(Child.class);
                        child.setId(doc.getId());
                        childrenList.add(child);
                        Boolean isMale = child.getSex().equals("Male");
                        Boolean isFemale = child.getSex().equals("Female");
                        if(isMale){
                            count_Male++;
                        } else if (isFemale) {
                            count_Female++;
                        }
                        String dateString = child.getBirthDate();
                        FormUtils formUtils = new FormUtils();
                        Date parsedDate = formUtils.parseDate(dateString);
                        int monthdiff = 60;

                        if (parsedDate != null) {
                            monthdiff = formUtils.calculateMonthsDifference(parsedDate);
                        }

                        if(monthdiff>=0 && monthdiff<=5){
                            age0_5++;
                        } else if (monthdiff>=6 && monthdiff<=11) {
                            age6_11++;
                        } else if (monthdiff>=12 && monthdiff<=23) {
                            age12_23++;
                        } else if (monthdiff>=24 && monthdiff<=35) {
                            age24_35++;
                        } else if (monthdiff>=36 && monthdiff<=47) {
                            age36_47++;
                        } else if (monthdiff>=48 && monthdiff<=59) {
                            age48_59++;
                        }

                    }

                    //-----------------Pie chart----------------//
                    PieChart pieChart = view.findViewById(R.id.pieChart);


                    ArrayList<PieEntry> pieEntries = new ArrayList<>();

                    pieEntries.add(new PieEntry(count_Male, String.valueOf(((double)count_Male/childrenList.size()) * 100) + "% - Male"));
                    pieEntries.add(new PieEntry(count_Female, String.valueOf(((double)count_Female/childrenList.size()) * 100) + "% - Female"));

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                    pieDataSet.setColors(lightBlueColor, darkBlueColor);
                    PieData pieData = new PieData(pieDataSet);
                    pieData.setDrawValues(false); // Enable drawing values (percentage)
                    pieChart.setData(pieData);

                    // Customize chart appearance
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setHoleColor(whiteColor);
                    pieChart.setTransparentCircleRadius(0f);
                    pieChart.setHoleRadius(50f);

                    pieChart.setUsePercentValues(true);
                    pieChart.setDrawCenterText(true);
                    pieChart.setCenterText("Gender Distribution");
                    pieChart.setCenterTextSize(14f);
                    pieChart.animateY(1500);

                    // Remove legend (color legend in the bottom part of the chart)
                    pieChart.getLegend().setEnabled(false);

                    pieChart.invalidate();
                    //------------------End of Pie chart-----------------------//

                    //-------------------Table 2----------------------------------//
                    String[] sex = {"Male", "Female"};
                    String[] testNumber = {String.valueOf(count_Male), String.valueOf(count_Female)};
                    String[] testPercentage = {String.valueOf((double) count_Male/childrenList.size() * 100) + " %",
                                                String.valueOf((double) count_Female/childrenList.size() * 100) + " %"};
                    TableLayout sextableLayout = view.findViewById(R.id.sextableLayout);
                    TableRow sexheaderRow = new TableRow(applicationContext);
                    TextView headerSex = new TextView(applicationContext);
                    TextView headerTotalSex = new TextView(applicationContext);
                    TextView headerPercentageSex = new TextView(applicationContext);


                    headerSex.setText("Sex");
                    headerTotalSex.setText("Total");
                    headerPercentageSex.setText("Percentage");
                    sexheaderRow.addView(headerSex);
                    sexheaderRow.addView(headerTotalSex);
                    sexheaderRow.addView(headerPercentageSex);

                    Costumize(sexheaderRow, headerSex, headerTotalSex, headerPercentageSex);
                    Gravity_Setter(headerTotalSex, headerPercentageSex);

                    sextableLayout.addView(sexheaderRow);

                    for (int i = 0; i < 2; i++) {
                        TableRow sextableRow = new TableRow(applicationContext);

                        TextView textView1 = new TextView(applicationContext);
                        TextView textView2 = new TextView(applicationContext);
                        TextView textView3 = new TextView(applicationContext);

                        Costumize(sextableRow, textView1, textView2, textView3);
                        setTextSizeAndPaddingForTextViews(18, 16, textView1, textView2, textView3);

                        textView1.setText(sex[i]);
                        textView2.setText(testNumber[i]);
                        textView3.setText(testPercentage[i]);
                        sextableRow.addView(textView1);
                        sextableRow.addView(textView2);
                        sextableRow.addView(textView3);

                        sextableLayout.addView(sextableRow);
                        setTextSizeAndPaddingForTextViews(18, 18, headerSex, headerPercentageSex,
                                headerTotalSex, textView1, textView2, textView3);
                        Gravity_Setter(textView2, textView3);
                    }
                    //-------------------End of Table 2---------------------------//


                    //------------------Bar Chart 2 --------------------------//

                    BarChart barChart2 = view.findViewById(R.id.barChart2);
                    // Sample data for the bar chart
                    //babaguhin natin yung first parameter sa entries.add from 1f, 2f, 3f, 4f, 5f -----> 0, 1, 2, 3, 4 para magdisplay ng word instead of numbers
                    ArrayList<BarEntry> entries2 = new ArrayList<>();
                    int[] ages_number = {age0_5, age6_11, age12_23, age24_35, age36_47, age48_59};
                    for(int i=0; i<6; i++){
                        entries2.add(new BarEntry(i, ages_number[i]));
                    }

                    BarDataSet barDataSet2 = new BarDataSet(entries2, "Bar Chart");
                    BarData barData2 = new BarData(barDataSet2);
                    barChart2.setData(barData2);

                    // Customize chart appearance
                    barDataSet2.setColors(ColorTemplate.PASTEL_COLORS);
                    barChart2.setFitBars(true);
                    //panglagay to ng title
                    barChart2.getDescription().setText("");
                    barChart2.animateY(2000);

                    // Customize X-axis and Y-axis
                    //pang pambago ng legend from numbers to text
                    final String[] labels2 = {"0-5", "6-11", "12-23", "24-35", "36-47", "48-59"};
                    XAxis xAxis2 = barChart2.getXAxis();
                    //eto ung function na magpapagana sa pampabago
                    xAxis2.setValueFormatter(new IndexAxisValueFormatter(labels2));
                    xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);

                    YAxis leftYAxis2 = barChart2.getAxisLeft();
                    YAxis rightYAxis2 = barChart2.getAxisRight();
                    leftYAxis2.setAxisMinimum(0f);
                    rightYAxis2.setAxisMinimum(0f);

                    // Remove legend (color legend in the bottom part of the chart)
                    barChart2.getLegend().setEnabled(false);

                    barChart2.invalidate();
                    //-----------------End of BarChart 2 ----------------------------//
                    //---- End of getting the sex  and age statistics ----//

                    //------Malnourished cases per categories -----//
                    String[] category = {"Underweight", "Severe Underweight", "Overweight", "Stunted", "Severe Stunted", "Wasted", "Severe Wasted"};
                    int[] numberCategory = {count_UB, count_SUB, count_OB, count_SB, count_SSB, count_WB, count_SWB};
                    TableLayout tableLayout3 = view.findViewById(R.id.tableLayout);
                    TableRow headerRowCategory = new TableRow(applicationContext);

                    TextView headerCategory = new TextView(applicationContext);
                    TextView headerTotalCategory = new TextView(applicationContext);

                    headerTotalCategory.setText("Total");
                    headerCategory.setText("Category");
                    headerRowCategory.addView(headerCategory);
                    headerRowCategory.addView(headerTotalCategory);

                    Costumize(headerRowCategory, headerCategory, headerTotalCategory);
                    setTextSizeAndPaddingForTextViews(18, 16, headerCategory, headerTotalCategory);
                    Gravity_Setter(headerTotalCategory);

                    tableLayout3.addView(headerRowCategory);

                    for (int i = 0; i < 7; i++) {
                        TableRow tableRow = new TableRow(applicationContext);

                        TextView textView1 = new TextView(applicationContext);
                        TextView textView2 = new TextView(applicationContext);
                        textView1.setText(category[i]);
                        textView2.setText(Integer.toString(numberCategory[i]));

                        setTextSizeAndPaddingForTextViews(18, 16, textView1, textView2);
                        tableRow.addView(textView1);
                        tableRow.addView(textView2);

                        Costumize(tableRow, textView1, textView2);
                        Gravity_Setter(textView2);
                        tableLayout3.addView(tableRow);
                    }

                    BarChart barChart = view.findViewById(R.id.barChart);
                    // Sample data for the bar chart
                    //babaguhin natin yung first parameter sa entries.add from 1f, 2f, 3f, 4f, 5f -----> 0, 1, 2, 3, 4 para magdisplay ng word instead of numbers
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    for(int i = 0; i<7; i++){
                        entries.add(new BarEntry(i, numberCategory[i]));
                    }

                    BarDataSet barDataSet = new BarDataSet(entries, "Bar Chart");
                    BarData barData = new BarData(barDataSet);
                    barChart.setData(barData);

                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    barChart.setFitBars(true);
                    barChart.getDescription().setText("Malnourished per month");
                    barChart.animateY(2000);

                    final String[] labels = {"U", "SU", "O", "S", "SS", "W", "SW"};
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                    YAxis leftYAxis = barChart.getAxisLeft();
                    YAxis rightYAxis = barChart.getAxisRight();
                    leftYAxis.setAxisMinimum(0f);
                    rightYAxis.setAxisMinimum(0f);

                    barChart.getLegend().setEnabled(false);
                    barChart.invalidate();
                    //----End of Malnourished cases per categories -------//


                    //Ranking the barangay by malnutrition rate
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
                    //Cases per barangay//
                    TableLayout tableLayout = view.findViewById(R.id.tableLayout3);
                    TableRow headerRow = new TableRow(applicationContext);
                    TextView headerBarangay = new TextView(applicationContext);
                    TextView headerTotal = new TextView(applicationContext);

                    headerBarangay.setText("Barangay");
                    headerTotal.setText("Total");

                    Costumize(headerRow, headerBarangay, headerTotal);
                    Gravity_Setter(headerTotal);
                    setTextSizeAndPaddingForTextViews(18, 16, headerBarangay, headerTotal);
                    headerRow.addView(headerBarangay);
                    headerRow.addView(headerTotal);

                    tableLayout.addView(headerRow);
                    for (int i = 23; i > 17; i--) {
                        TableRow tableRow = new TableRow(applicationContext);

                        TextView textView1 = new TextView(applicationContext);
                        textView1.setText(barangay[index[i]]);

                        TextView textView2 = new TextView(applicationContext);
                        textView2.setText(Integer.toString(malnutrition_perbarangay[i]));

                        Costumize(tableRow, textView1, textView2);
                        Gravity_Setter(textView2);
                        setTextSizeAndPaddingForTextViews(18, 16, textView1, textView2);
                        tableRow.addView(textView1);
                        tableRow.addView(textView2);
                        tableLayout.addView(tableRow);
                    }

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

    private void setTextSizeAndPaddingForTextViews(float textSize, int padding, TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            textView.setPadding(padding, padding, padding, padding);
        }
    }
    private void Costumize(TableRow tableRow, TextView... textViews){
        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        // Create the first TextView (first column)

        for (TextView textView : textViews){
            textView.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }
    }

    private void Gravity_Setter(TextView... textViews){
        for(TextView textView : textViews){
            textView.setGravity(Gravity.END);
        }
    }
}