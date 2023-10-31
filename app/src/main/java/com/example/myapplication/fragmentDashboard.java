package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class fragmentDashboard extends Fragment {
    View view;
    FirebaseFirestore db;
    public void onAttach(Context context) {
        super.onAttach(context);
        FirebaseApp.initializeApp(requireContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        db = FirebaseFirestore.getInstance();


        String [] barangay = getResources().getStringArray(R.array.barangay);
        TextView priority = view.findViewById(R.id.textPriority);
        TextView totalAssesment = view.findViewById(R.id.txtTotalAssessment);
        TextView totalMalnourished = view.findViewById(R.id.txtTotalMalnourished);
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
                    int[] index = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
                    int count_U, count_SU, count_N, count_W, count_SW, count_S, count_SS, count_O;
                    int count_UB, count_SUB, count_NB, count_WB, count_SWB, count_SB, count_SSB, count_OB;
                    count_UB = count_SUB = count_NB = count_WB = count_SWB = count_SB = count_SSB = count_OB = 0;
                    for (int i = 0; i < 24; i++) {
                        count_U = 0;
                        count_SU = 0;
                        count_N = 0;
                        count_W = 0;
                        count_SW = 0;
                        count_S = 0;
                        count_SS = 0;
                        count_O = 0;
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

                    //Toast.makeText(requireContext(), "Male " + count_Male + " Female " + count_Female , Toast.LENGTH_SHORT).show();

                    //---- End of getting the sex  and age statistics ----//




                    //------Malnourished cases per categories -----//


                    //----End of Malnourished cases per categories -------//
                    //<------------------------------------------------------->

                    for(int i=0;i<24;i++){
                        malnutrition_rate[i] = record[i][0]+record[i][2]+record[i][3]+record[i][5]+record[i][6]+record[i][7];
                        malnutrition_rate[i] = malnutrition_rate[i]/task.getResult().size();
                        malnutrition_rate[i] = malnutrition_rate[i]*100;
                    }

                    int wow = 0;
                    for(int i=0;i<24;i++){
                        wow = wow + record[i][2] + record[i][6] + record[i][7];
                    }

                    int wew = 0;
                    for(int i=0;i<24;i++){
                        wew = wew + record[i][0]+record[i][2]+record[i][3]+record[i][5]+record[i][6]+record[i][7] + record[i] [4];
                    }

                    priority.setText(String.valueOf(wow));
                    totalAssesment.setText(String.valueOf(task.getResult().size()));
                    totalMalnourished.setText(String.valueOf(wew));


                    //Ranking the barangay by malnutrition rate
                    for (int i = 0; i < malnutrition_rate.length - 1; i++) {
                        for (int j = i + 1; j < malnutrition_rate.length; j++) {
                            if (malnutrition_rate[i] > malnutrition_rate[j]) {
                                // Swap malnutrition_rate[i] and malnutrition_rate[j]
                                double tempRate = malnutrition_rate[i];
                                malnutrition_rate[i] = malnutrition_rate[j];
                                malnutrition_rate[j] = tempRate;

                                // Swap index[i] and index[j]
                                int tempIndex = index[i];
                                index[i] = index[j];
                                index[j] = tempIndex;
                            }
                        }
                    }

                    //-- Top 6 barangay with highest Malnutrition ----//
                    String edi_wow = " ";
                    for (int i=23;i>17; i--){
                        edi_wow = edi_wow + barangay[index[i]] + " " + String.valueOf(malnutrition_rate[i]) + " ";
                    }

                    //Toast.makeText(requireContext(), "Barangay " + barangay[index[23]] + " :" + malnutrition_rate[23], Toast.LENGTH_SHORT).show();

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

        View priorityView = view.findViewById(R.id.priorityView);

        priorityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();

                // Replace the current fragment with the new fragment
                fragmentTransaction.replace(R.id.frameLayoutAdmin, new fragmentPriority());

                // Add the transaction to the back stack
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}