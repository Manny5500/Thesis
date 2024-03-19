package com.example.myapplication;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class fragment_sr_sum extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_sr_sum, container, false);
        String[][] OPTData = new String[4][2];
        String[][] MotherData = new String[5][2];
        String[][] DataData = new String[5][2];
        TableLayout OPTTL = view.findViewById(R.id.OPTTL);
        TableLayout MotherTL = view.findViewById(R.id.MotherTL);
        TableLayout DataTL = view.findViewById(R.id.DataTL);
        String [] headers =  {"Summary of Children covered by e-OPT Plus", "Mothers/Caregivers Summary", "Data Summary:"};
        String [] OPTCat = {"#Children 0-59 mos. affected by Undernutrition", "#Children 0-59 mos. with Overweight/Obesity:",
        "Total Number of Children 0-23 mos. old: ", "#Children 0-23 mos. affected by Undernutrition: "};

        String [] MotherCat = {"Total Number of M/Cs of children 0-59 mos. old: ", "#M/Cs of 0-59 mos children affected by Undernutrition",
                "#M/Cs of 0-59 mos. children with Overweight/Obesity: ", "Total Number of M/Cs of children 0-23 mos. old ",
            "#M/Cs of 0-23 mos. children affected by Undernutrition: "};

        String [] DataCat = {"#Children with weight but no height: ", "#Children with height but no weight: ", "#Children with missing information:",
        "#Children with names repeated: ", "#Children older than 59 months"};


        int l=1;
        for(int i=0; i<4; i++){
            OPTData[i][0] = OPTCat[i];
            OPTData[i][1] = "1";
        }
        for(int i=0; i<5; i++){
            MotherData[i][0] = MotherCat[i];
            MotherData[i][1] = "1";

            DataData[i][0] = DataCat[i];
            DataData[i][1] = "1";
        }

        generateTable(OPTTL,headers[0], OPTData);
        generateTable(MotherTL, headers[1], MotherData);
        generateTable(DataTL, headers[2], DataData);
        return view;
    }
    private void generateTable(TableLayout tableLayout, String header, String[][] data) {
        TableRow headerRow = new TableRow(requireContext());


        TextView headerTextView = new TextView(requireContext());
        headerTextView.setText(header);
        headerTextView.setTypeface(null, Typeface.BOLD);
        TableSetter.Costumize(headerRow, headerTextView);
        headerTextView.setGravity(Gravity.CENTER);
        TableSetter.setTextSizeAndPaddingForTextViews(18, 16, headerTextView);
        headerRow.addView(headerTextView);


        tableLayout.addView(headerRow);

        // Populate table rows with data
        for (String[] rowData : data) {
            TableRow tableRow = new TableRow(requireContext());

            // Create cells for each column in the row
            for (int i= 0; i<rowData.length; i++){
                TextView cellTextView = new TextView(requireContext());
                cellTextView.setText(rowData[i]);
                TableSetter.Costumize(tableRow, cellTextView);
                if(i==0){
                    cellTextView.setGravity(Gravity.START);
                }else{
                    cellTextView.setGravity(Gravity.END);
                }
                TableSetter.setTextSizeAndPaddingForTextViews(18, 16, cellTextView);
                tableRow.addView(cellTextView);
            }
            tableLayout.addView(tableRow);
        }
    }
}