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

public class fragmentUM extends Fragment implements UMAdapter.ItemClickListener {
    private RecyclerView umrecyclerView;
    private UMAdapter adapter;
    private SearchView searchView;
    private List<ComponentModel> originalComponents; // Original data list
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_u_m, container, false);

        searchView = view.findViewById(R.id.umsearchView);
        searchView.clearFocus();
        umrecyclerView = view.findViewById(R.id.UserManagementRecycler);
        umrecyclerView.setHasFixedSize(true);
        umrecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize the original data list
        originalComponents = generateComponents();

        adapter = new UMAdapter(requireContext(), originalComponents, this);
        umrecyclerView.setAdapter(adapter);

        // Set up the search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Called when the user submits the query (e.g., presses the search button)
                //performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Called when the query text is changed (e.g., user types or deletes characters)
                // You can update suggestions or perform real-time filtering here
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
        components.add(new ComponentModel("Awaw", R.color.black, R.color.white));
        components.add(new ComponentModel("Rose", R.color.black, R.color.white));
        components.add(new ComponentModel("Gagi", R.color.black, R.color.white));
        components.add(new ComponentModel("Meow meow", R.color.black, R.color.white));
        components.add(new ComponentModel("Ha ha", R.color.black, R.color.white));
        components.add(new ComponentModel("fsdafsda", R.color.black, R.color.white));
        components.add(new ComponentModel("Hello World", R.color.black, R.color.white));
        components.add(new ComponentModel("Rawr", R.color.black, R.color.white));
        components.add(new ComponentModel("Wowayts", R.color.black, R.color.white));
        components.add(new ComponentModel("Goning", R.color.black, R.color.white));

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