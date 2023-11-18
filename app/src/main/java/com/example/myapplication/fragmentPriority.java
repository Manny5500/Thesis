package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fragmentPriority extends Fragment {
    View view;
    FirebaseFirestore db;
    RecyclerView recyclerView;

    private ChildAdapter userAdapter;
    int whiteColor;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FirebaseApp.initializeApp(requireContext());
        whiteColor = ContextCompat.getColor(context, R.color.viola);
        userAdapter = new ChildAdapter(requireContext(), new ArrayList<>());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_priority, container, false);
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycler);

        Populate();

        SearchView searchView = view.findViewById(R.id.searchView);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        searchEditText.setTextColor(whiteColor);
        searchEditText.setHintTextColor(whiteColor);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                userAdapter.getFilter().filter(s);
                return true;
            }
        });

        return view;
    }

    public void Populate(){
        db.collection("children").whereArrayContainsAny("statusdb", Arrays.asList("Severe Wasted", "Severe Stunted", "Severe Underweight")).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Child> arrayList = new ArrayList<>();

                    for (QueryDocumentSnapshot doc: task.getResult()){
                        Child child = doc.toObject(Child.class);
                        child.setId(doc.getId());
                        arrayList.add(child);
                        db.collection("users").whereEqualTo("user","parent").
                                whereEqualTo("email", child.getGmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(QueryDocumentSnapshot doc1:task.getResult()){
                                    User user = doc1.toObject(User.class);
                                    child.setPhoneNumber(user.getContact());
                                }
                            }
                        });
                    }

                    userAdapter = new ChildAdapter(getContext(), arrayList);
                    recyclerView.setAdapter(userAdapter);

                    userAdapter.setOnItemClickListener(new ChildAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(Child child) {
                            App.child = child;
                            startActivity(new Intent(requireContext(), PriorityClicked.class));
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed to get all users", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        Populate();
    }

}