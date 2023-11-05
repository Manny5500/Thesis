package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FireStoreUtility {
    public static void getBarangayStatus(String barangayString, TextView textView, FirebaseFirestore db){
        db.collection("barangay").document(barangayString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();

                    String feedto = String.valueOf(data.get("feedto"));
                    FormUtils formUtils = new FormUtils();
                    Date parsedDate = formUtils.parseDate(feedto);


                    int daydiff = 3;
                    if (parsedDate != null) {
                        daydiff = formUtils.calculateDaysDifference(parsedDate);
                    } else {

                    }
                    if(data!=null && data.containsKey("feedfrom") && daydiff<=0){
                        textView.setTextColor(Color.parseColor("#FF0000"));

                    }else if(data!=null && data.containsKey("feedfrom") && daydiff>0){
                        resetFeedingDate(barangayString, "", "", db);
                    }

                } else {
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public static  void getBarangayDetails(String barangayString, Dialog dialog, FirebaseFirestore db, Context context){
        db.collection("barangay").document(barangayString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    Button set = dialog.findViewById(R.id.btnSet);
                    Button cancel = dialog.findViewById(R.id.btnCancel);
                    TextInputEditText from = dialog.findViewById(R.id.dateFrom);
                    TextInputEditText to = dialog.findViewById(R.id.dateTo);
                    if(data!=null && data.containsKey("feedfrom")){
                        from.setText(String.valueOf(data.get("feedfrom")));
                        to.setText(String.valueOf(data.get("feedto")));
                    }
                    FormUtils.dateClicked(from, context);
                    FormUtils.dateClicked(to, context);

                    set.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String fromValue = from.getText().toString().trim();
                            String toValue = to.getText().toString().trim();
                            setFeedingDate(barangayString, fromValue, toValue, db, context);
                            dialog.dismiss();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                } else {
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle errors here
            }
        });
    }

    private static void setFeedingDate(String barangayString, String fromValue, String toValue, FirebaseFirestore db, Context context){
        Map<String, Object> barangay = new HashMap<>();
        barangay.put("feedfrom", fromValue);
        barangay.put("feedto", toValue);
        db.collection("barangay").document(barangayString).set(barangay).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                settoChildren(barangayString,db, "set");
                Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to save changes", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private static void resetFeedingDate(String barangayString, String fromValue, String toValue, FirebaseFirestore db){
        Map<String, Object> barangay = new HashMap<>();
        barangay.put("feedfrom", fromValue);
        barangay.put("feedto", toValue);
        db.collection("barangay").document(barangayString).set(barangay).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                settoChildren(barangayString,db, "reset");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
    private static void settoChildren(String barangayString, FirebaseFirestore db, String action){
        CollectionReference collectionRef = db.collection("children");
        Query query = collectionRef
                .whereEqualTo("barangay", barangayString)
                .whereArrayContainsAny("statusdb", Arrays.asList("Underweight", "Wasted", "Stunted"));
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                WriteBatch batch = db.batch();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    DocumentReference documentRef = collectionRef.document(document.getId());
                    if(action.equals("set")) {
                        batch.update(documentRef, "forfeeding", "Yes");
                    } else if (action.equals("reset")) {
                        batch.update(documentRef, "forfeeding", "No");
                    }
                }
                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        });
    }
}
