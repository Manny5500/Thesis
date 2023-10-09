package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class fragmentPriority extends Fragment implements PriorityAdapter.ItemClickListener{
    View view;
    private RecyclerView recyclerView;
    private PriorityAdapter adapter;



    String item[];
    String name[];
    String pnumber[];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_priority, container, false);

        item = getResources().getStringArray(R.array.malnourished_categories);
        name = getResources().getStringArray(R.array.children_name);
        pnumber = getResources().getStringArray(R.array.phone_number);

        recyclerView = view.findViewById(R.id.recyclerPriority);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<ComponentModel> items = generateComponents();
        adapter = new PriorityAdapter(requireContext(), items, this);
        recyclerView.setAdapter(adapter);

        return view;

    }

    private List<ComponentModel> generateComponents() {
        List<ComponentModel> components = new ArrayList<>();
        for (int i = 0 ; i<item.length; i++){
            if (item[i].equals("Severe Wasted"))
            {
                components.add(new ComponentModel(item[i], R.color.black,  R.color.background1, name[i], pnumber[i]));
            }else if(item[i].equals("Severe Underweight")){
                components.add(new ComponentModel(item[i], R.color.black, R.color.background2, name[i], pnumber[i]));
            }else if (item[i].equals("Severe Stunted")) {
                components.add(new ComponentModel(item[i], R.color.black, R.color.background3, name[i], pnumber[i]));
            }
        }
        return components;
    }

    @Override
    public void onItemClick(int position) {
        ComponentModel clickedItem = adapter.getItem(position);
        if (clickedItem != null) {
            Intent intent = new Intent(requireContext(), PriorityClicked.class);
            intent.putExtra("name", clickedItem.getName());
            intent.putExtra("status", clickedItem.getText());
            intent.putExtra("pnumber", clickedItem.getPnumber());
            //Toast.makeText(requireContext(), "Clicked: " + clickedItem.getText(), Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }

}