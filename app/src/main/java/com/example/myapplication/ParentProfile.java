package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class ParentProfile extends Fragment {
    private String gmail;
    private FirebaseFirestore db;
    private String age, name, address, email, contact;
    TextView textage, textname, textaddress, textemail, textcontact;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_parent_profile, container, false);
        db = FirebaseFirestore.getInstance();
        textage = view.findViewById(R.id.textParentProfileAge);
        textname = view.findViewById(R.id.textParentProfileName);
        textaddress = view.findViewById(R.id.textParentProfileAddress);
        textemail = view.findViewById(R.id.textParentProfileEmail);
        textcontact = view.findViewById(R.id.textParentProfileContact);

        gmail = ((ParentActivity)getActivity()).email;
        if(!gmail.isEmpty()) {
           getProfile();
        }
        return view;
    }

    private void getProfile(){
        db.collection("users").whereEqualTo("email", gmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        name = doc.getString("firstName") + " " + doc.getString("middleName") + " " + doc.getString("lastName");
                        age = doc.getString("birthdate");
                        FormUtils formUtils = new FormUtils();
                        Date parsedDate = formUtils.parseDate(age);
                        if (parsedDate != null) {
                            age = String.valueOf(formUtils.calculateMonthsDifference(parsedDate)/12);
                        } else {
                            Toast.makeText(requireContext(), "Failed to parse the date", Toast.LENGTH_SHORT).show();
                        }
                        address = doc.getString("barangay") + ", Magdalena, Laguna";
                        email = doc.getString("email");
                        contact = doc.getString("contact");
                    }
                    displayProfile(name, age, address, email, contact);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed to get all users", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayProfile(String name, String age, String address, String email, String contact){
        textage.setText(age);
        textname.setText(name);
        textaddress.setText(address);
        textemail.setText(email);
        textcontact.setText(contact);
    }

}