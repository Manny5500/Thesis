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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;



public class fragmentDashboard extends Fragment {
    View view;
    TextView priority, totalAssessment, totalMalnourished;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FirebaseApp.initializeApp(requireContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        db = FirebaseFirestore.getInstance();
        View recommendation = view.findViewById(R.id.ViewRecommendation);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        }else{
            // Toast.makeText(this, ""+ user.getEmail(), Toast.LENGTH_SHORT).show();
        }

        priority = view.findViewById(R.id.textPriority);
        totalAssessment = view.findViewById(R.id.txtTotalAssessment);
        totalMalnourished = view.findViewById(R.id.txtTotalMalnourished);
        recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), RecommendationAdmin.class);
                startActivity(intent);
            }
        });

        dashboardData();
        View priorityView = view.findViewById(R.id.priorityView);
        priorityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutAdmin, new fragmentPriority());
                //fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void dashboardData(){
        db.collection("children").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Child> childrenList = new ArrayList<>();
                    int count_P = 0, count_M = 0;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Child child = doc.toObject(Child.class);
                        child.setId(doc.getId());
                        childrenList.add(child);
                        Boolean isUnderWeight = child.getStatusdb().contains("Underweight");
                        Boolean isNormal = child.getStatusdb().contains("Normal");
                        Boolean isSevereUnderweight = child.getStatusdb().contains("Severe Underweight");
                        Boolean isWasted = child.getStatusdb().contains("Wasted");
                        Boolean isSevereWasted = child.getStatusdb().contains("Severe Wasted");
                        Boolean isStunted = child.getStatusdb().contains("Stunted");
                        Boolean isSevereStunted = child.getStatusdb().contains("Severe Stunted");
                        Boolean isOverweight = child.getStatusdb().contains("Overweight");
                        if (isSevereStunted||isSevereWasted||isSevereUnderweight){
                            count_P++;
                        }
                        if (!isNormal){
                            count_M++;
                        }
                    }
                    priority.setText(String.valueOf(count_P));
                    totalAssessment.setText(String.valueOf(task.getResult().size()));
                    totalMalnourished.setText(String.valueOf(count_M));
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

    }
}