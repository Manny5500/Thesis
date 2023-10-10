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

public class parent_notification extends Fragment implements NotifyAdapter.ItemClickListener {


    private RecyclerView recyclerView;
    private NotifyAdapter adapter;
    View view;

    String item[];
    String name[];
    String pnumber[];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_notification, container, false);

        item = getResources().getStringArray(R.array.malnourished_categories);
        name = getResources().getStringArray(R.array.children_name);
        pnumber = getResources().getStringArray(R.array.phone_number);

        recyclerView = view.findViewById(R.id.recyclerNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<ComponentModel> items = generateComponents();
        adapter = new NotifyAdapter(requireContext(), items, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<ComponentModel> generateComponents() {
        List<ComponentModel> components = new ArrayList<>();
        components.add(new ComponentModel("Your child is severely malnourished", R.color.black, R.color.background3));
        components.add(new ComponentModel("Your child is severely malnourished", R.color.black, R.color.background3));
        components.add(new ComponentModel("Your child is severely malnourished", R.color.black, R.color.background3));
        components.add(new ComponentModel("Your child is severely malnourished", R.color.black, R.color.background3));
        return components;
    }

    @Override
    public void onItemClick(int position) {
        ComponentModel clickedItem = adapter.getItem(position);
        if (clickedItem != null) {
            //Intent intent = new Intent(MainActivity.this, DynamicActivity.class);
            //startActivity(intent);
            Toast.makeText(requireContext(),"Notify clicked", Toast.LENGTH_SHORT).show();
        }
    }
}