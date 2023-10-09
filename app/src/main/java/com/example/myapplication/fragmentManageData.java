package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class fragmentManageData extends Fragment implements ChildAdapter.ItemClickListener {
    View view;
    private RecyclerView umrecyclerView;
    private ChildAdapter adapter;
    private SearchView searchView;
    private List<ComponentModel> originalComponents;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_data, container, false);
        searchView = view.findViewById(R.id.manageDataSearchView);
        umrecyclerView = view.findViewById(R.id.manageDataRecyclerView);
        umrecyclerView.setHasFixedSize(true);
        umrecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        originalComponents = generateComponents();
        adapter = new ChildAdapter(requireContext(), originalComponents, this);
        umrecyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });

        return view;
    }

    private List<ComponentModel> generateComponents() {
        List<ComponentModel> components = new ArrayList<>();
        // Populate your components list here
        // For example:
        components.add(new ComponentModel("Marcus Abad", R.color.black, R.color.white));
        components.add(new ComponentModel("Jose Santos", R.color.black, R.color.white));
        components.add(new ComponentModel("Pedro Aguilar", R.color.black, R.color.white));
        components.add(new ComponentModel("Isidro Calacuchi", R.color.black, R.color.white));
        components.add(new ComponentModel("Alvin Esperanza", R.color.black, R.color.white));
        components.add(new ComponentModel("Joan Cruz", R.color.black, R.color.white));
        components.add(new ComponentModel("Jenny Torres", R.color.black, R.color.white));
        components.add(new ComponentModel("Samantha Chan", R.color.black, R.color.white));
        components.add(new ComponentModel("Sabrina San Jose", R.color.black, R.color.white));
        components.add(new ComponentModel("Serena Panganiban", R.color.black, R.color.white));

        return components;
    }

    private void performSearch(String query) {
        List<ComponentModel> filteredComponents = new ArrayList<>();
        for (ComponentModel component: originalComponents){
            if (component.getText().toLowerCase().contains(query.toLowerCase())) {
                filteredComponents.add(component);
            }
        }
        adapter.setComponents(filteredComponents);

    }


    @Override
    public void onItemClick(int position, String action) {
        ComponentModel clickedItem = adapter.getItem(position);

        if (clickedItem != null) {
            if ("edit".equals(action)) {
                Toast.makeText(requireContext(), "Edited: " + clickedItem.getText(), Toast.LENGTH_SHORT).show();
            } else if ("delete".equals(action)) {
                Toast.makeText(requireContext(), "Deleted: " + clickedItem.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}