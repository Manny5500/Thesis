package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class FragmentProfiling extends Fragment {

    View view;
    TextView namText, polBod, chilBod;
    MaterialCardView polView, chilView;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profiling, container, false);
        db = FirebaseFirestore.getInstance();
        namText = view.findViewById(R.id.namText);
        polBod = view.findViewById(R.id.polBod);
        chilBod = view.findViewById(R.id.chilBod);
        polView = view.findViewById(R.id.polView);
        chilView = view.findViewById(R.id.chilView);
        namText.setText(App.user.getBarangay());
        setPopulation();
        cBevent();
        pBevent();
        return view;
    }

    private void setPopulation(){
        DocumentReference docRefs = db.collection("barangay").document(App.user.getBarangay());
        docRefs.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        BarangayModel bM = document.toObject(BarangayModel.class);
                        bM.setBarangay(document.getId());
                        if(bM.getEstimatedChildren()>0){
                            chilBod.setText(String.valueOf(bM.getEstimatedChildren()));
                        }
                        if(bM.getPopulation()>0){
                            polBod.setText(String.valueOf(bM.getPopulation()));
                        }
                    } else {
                        Log.d("Firetore No Docu", "No such document");
                    }
                } else {
                    Log.e("Firestore Exception", "get failed with ", task.getException());
                }
            }
        });
    }

    private void cBevent(){
        chilBod.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    private void pBevent(){
        polBod.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

}