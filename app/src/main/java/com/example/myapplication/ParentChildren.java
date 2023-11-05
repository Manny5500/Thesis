package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ParentChildren extends Fragment {
    View view;

    private String gmail;
    private FirebaseFirestore db;
    private ArrayList<Child> childrenList;
    private int currentIndex = 0;
    TextView childName, age, status, textCounts, textRecommendations;
    String dateString;
    int monthdiff;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Initialize FirebaseApp when the fragment is attached to a context
        FirebaseApp.initializeApp(requireContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_children, container, false);
        db = FirebaseFirestore.getInstance();
        childrenList = new ArrayList<>();
        childName = view.findViewById(R.id.textChildName);
        age = view.findViewById(R.id.textAge);
        status = view.findViewById(R.id.textStatus);
        textCounts = view.findViewById(R.id.textCounts);
        textRecommendations = view.findViewById(R.id.textRecommendations);

        gmail = ((ParentActivity)getActivity()).email;
        if(!gmail.isEmpty()) {
            db.collection("children").whereEqualTo("gmail", gmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Child child = doc.toObject(Child.class);
                            child.setId(doc.getId());
                            childrenList.add(child);
                        }

                        displayChildData(currentIndex);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(requireContext(), "Failed to get all users", Toast.LENGTH_SHORT).show();
                }
            });

        }



        ImageView nextButton = view.findViewById(R.id.btnnext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex < childrenList.size() - 1) {
                    currentIndex++;
                    displayChildData(currentIndex);
                } else {
                    // Handle the case when there are no more children to display
                    Toast.makeText(requireContext(), "No more children to display", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle back button click
        ImageView backButton = view.findViewById(R.id.btnprev);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex > 0) {
                    currentIndex--;
                    displayChildData(currentIndex);
                } else {
                    // Handle the case when the user is at the first child
                    Toast.makeText(requireContext(), "Already at the first child", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void displayChildData(int index) {
        Child child = childrenList.get(index);
        childName.setText(child.getChildFirstName()+" " + child.getChildLastName());
        dateString = child.getBirthDate();
        FormUtils formUtils = new FormUtils();
        Date parsedDate = formUtils.parseDate(dateString);
        if (parsedDate != null) {
            monthdiff = formUtils.calculateMonthsDifference(parsedDate);
        } else {
            Toast.makeText(requireContext(), "Failed to parse the date", Toast.LENGTH_SHORT).show();
        }


        if(monthdiff == 1){
            age.setText(String.valueOf(monthdiff)+ " " + "month");
        }else{
            age.setText(String.valueOf(monthdiff)+ " " + "months");
        }

        ArrayList<String> statusList = child.getStatusdb();
        //ArrayList<String> recommendation = new ArrayList<>();
        Set<String> recommendation = new HashSet<>();

        StringBuilder statusStringBuilder = new StringBuilder();
        StringBuilder statusStringBuilder2 = new StringBuilder();
        for (String status : statusList) {
            statusStringBuilder.append(status).append("\n");
        }


        recommendation = FindStatusWFA.Recommendations(statusList,monthdiff);

        if(String.valueOf(child.getForfeeding()).equals("Yes") && monthdiff>=24){
            recommendation.add("Your child is part of the feeding program.");
        }

        for (String recommend: recommendation){
            if (!recommend.endsWith("\n")) {
                statusStringBuilder2.append("\t\t\t\t\t\t*").append(recommend).append("\n");
            } else {
                statusStringBuilder2.append("\t\t\t\t\t\t*").append(recommend);
            }
        }
        textCounts.setText("Child " + String.valueOf(currentIndex+1) + " of " + String.valueOf(childrenList.size()));
        status.setText(statusStringBuilder.toString());
        textRecommendations.setText(statusStringBuilder2.toString());
    }

}
