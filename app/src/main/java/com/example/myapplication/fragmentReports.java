package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

public class fragmentReports extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_reports, container, false);

        BarChart barChart = view.findViewById(R.id.barChart);
        //Try lang to sa adapter
        Random rand = new Random();
        int upperbound = 25;

        // Sample data for the bar chart
        //babaguhin natin yung first parameter sa entries.add from 1f, 2f, 3f, 4f, 5f -----> 0, 1, 2, 3, 4 para magdisplay ng word instead of numbers
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 40f));
        entries.add(new BarEntry(1, 30f));
        entries.add(new BarEntry(2, 60f));
        entries.add(new BarEntry(3, 80f));
        entries.add(new BarEntry(4, 10f));
        entries.add(new BarEntry(5, 20f));
        entries.add(new BarEntry(6, 70f));
        entries.add(new BarEntry(7, 50f));


        BarDataSet barDataSet = new BarDataSet(entries, "Bar Chart");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        // Customize chart appearance
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setFitBars(true);
        //panglagay to ng title
        barChart.getDescription().setText("Malnourished per month");
        barChart.animateY(2000);

        // Customize X-axis and Y-axis
        //pang pambago ng legend from numbers to text
        final String[] labels = {"Feb", "March", "Apr", "May", "Jun", "Jul", "Aug", "Sep"};
        XAxis xAxis = barChart.getXAxis();
        //eto ung function na magpapagana sa pampabago
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftYAxis = barChart.getAxisLeft();
        YAxis rightYAxis = barChart.getAxisRight();
        leftYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMinimum(0f);

        // Remove legend (color legend in the bottom part of the chart)
        barChart.getLegend().setEnabled(false);

        barChart.invalidate();


        //-----------------Pie chart----------------//
        PieChart pieChart = view.findViewById(R.id.pieChart);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(28f, "28% - Female"));
        pieEntries.add(new PieEntry(72f, "72% - Male"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(getResources().getColor(R.color.colorLightBlue),
                getResources().getColor(R.color.colorDarkBlue));


        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false); // Enable drawing values (percentage)
        pieChart.setData(pieData);

        // Customize chart appearance
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(android.R.color.white));
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setHoleRadius(50f);

        pieChart.setUsePercentValues(true);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("Gender Distribution");
        pieChart.setCenterTextSize(14f);
        pieChart.animateY(1500);


        // Remove legend (color legend in the bottom part of the chart)
        pieChart.getLegend().setEnabled(false);

        pieChart.invalidate();
        //------------------End of Pie chart-----------------------//


        //------------------Bar Chart 2 --------------------------//

        BarChart barChart2 = view.findViewById(R.id.barChart2);

        // Sample data for the bar chart
        //babaguhin natin yung first parameter sa entries.add from 1f, 2f, 3f, 4f, 5f -----> 0, 1, 2, 3, 4 para magdisplay ng word instead of numbers
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(0, 40f));
        entries2.add(new BarEntry(1, 30f));
        entries2.add(new BarEntry(2, 60f));
        entries2.add(new BarEntry(3, 80f));
        entries2.add(new BarEntry(4, 10f));



        BarDataSet barDataSet2 = new BarDataSet(entries2, "Bar Chart");
        BarData barData2 = new BarData(barDataSet2);
        barChart2.setData(barData2);

        // Customize chart appearance
        barDataSet2.setColors(ColorTemplate.PASTEL_COLORS);
        barChart2.setFitBars(true);
        //panglagay to ng title
        barChart2.getDescription().setText("Malnourished per category");
        barChart2.animateY(2000);

        // Customize X-axis and Y-axis
        //pang pambago ng legend from numbers to text
        final String[] labels2 = {"0", "1", "2", "3", "4"};
        XAxis xAxis2 = barChart2.getXAxis();
        //eto ung function na magpapagana sa pampabago
        xAxis2.setValueFormatter(new IndexAxisValueFormatter(labels2));
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftYAxis2 = barChart2.getAxisLeft();
        YAxis rightYAxis2 = barChart2.getAxisRight();
        leftYAxis2.setAxisMinimum(0f);
        rightYAxis2.setAxisMinimum(0f);

        // Remove legend (color legend in the bottom part of the chart)
        barChart2.getLegend().setEnabled(false);

        barChart2.invalidate();
        //-----------------End of BarChart 2 ----------------------------//

        //-------------------Table 1---------------------------------//
        // Get a reference to the TableLayout

        String[] barangay = {"Alipit", "Bucal", "Cigara", "Maravilla", "Poblacion", "Tipunan"};
        TableLayout tableLayout = view.findViewById(R.id.tableLayout);
        // Create a header TableRow
        TableRow headerRow = new TableRow(requireContext());
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        // Create the header for the first column ("Age")
        TextView headerAge = new TextView(requireContext());
        headerAge.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        headerAge.setText("Barangay");
        headerRow.addView(headerAge);

        // Create the header for the second column ("Total")
        TextView headerTotal = new TextView(requireContext());
        headerTotal.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        headerTotal.setGravity(Gravity.END); // Align header text to the right
        headerTotal.setText("Total");
        headerRow.addView(headerTotal);

        // Add the header TableRow to the TableLayout
        tableLayout.addView(headerRow);
        // Create the table dynamically with 2 columns and 6 rows
        for (int i = 0; i < 6; i++) {
            // Create a new TableRow
            TableRow tableRow = new TableRow(requireContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Create the first TextView (first column)
            TextView textView1 = new TextView(requireContext());
            textView1.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Layout weight is set to 1f
            textView1.setText(barangay[i]);

            // Create the second TextView (second column)
            TextView textView2 = new TextView(requireContext());
            textView2.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Layout weight is set to 1f
            textView2.setGravity(Gravity.END); // Align text to the right
            textView2.setText(Integer.toString(rand.nextInt(upperbound)));


            setTextSizeAndPaddingForTextViews(18, 16, textView1, textView2);
            // Add TextViews to the TableRow
            tableRow.addView(textView1);
            tableRow.addView(textView2);

            // Add the TableRow to the TableLayout
            tableLayout.addView(tableRow);
        }
        //-------------------End of Table 1---------------------------//



        //-------------------Table 2----------------------------------//
        String[] sex = {"Male", "Female"};
        String[] testNumber = {"144", "56"};
        String[] testPercentage = {"72%", "28%"};
        TableLayout sextableLayout = view.findViewById(R.id.sextableLayout);
        // Create a header TableRow
        TableRow sexheaderRow = new TableRow(requireContext());

        sexheaderRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        // Create the header for the first column ("Age")
        TextView headerSex = new TextView(requireContext());
        headerSex.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        headerSex.setText("Sex");
        sexheaderRow.addView(headerSex);

        // Create the header for the second column ("Total")
        TextView headerTotalSex = new TextView(requireContext());
        headerTotalSex.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        headerTotalSex.setGravity(Gravity.END); // Align header text to the right
        headerTotalSex.setText("Total");
        sexheaderRow.addView(headerTotalSex);

        // Create the header for the third column ("Percentage")
        TextView headerPercentageSex = new TextView(requireContext());
        headerPercentageSex.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        headerPercentageSex.setGravity(Gravity.END); // Align header text to the right
        headerPercentageSex.setText("Percentage");
        sexheaderRow.addView(headerPercentageSex);


        // Add the header TableRow to the TableLayout
        sextableLayout.addView(sexheaderRow);
        // Create the table dynamically with 2 columns and 6 rows
        for (int i = 0; i < 2; i++) {
            // Create a new TableRow
            TableRow sextableRow = new TableRow(requireContext());
            sextableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Create the first TextView (first column)
            TextView textView1 = new TextView(requireContext());
            textView1.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Layout weight is set to 1f

            textView1.setText(sex[i]);

            // Create the second TextView (second column)
            TextView textView2 = new TextView(requireContext());
            textView2.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Layout weight is set to 1f

            textView2.setGravity(Gravity.END); // Align text to the right
            textView2.setText(testNumber[i]);

            //Create the thrid TextView
            TextView textView3 = new TextView(requireContext());
            textView3.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Layout weight is set to 1f
            textView3.setGravity(Gravity.END); // Align text to the right
            textView3.setText(testPercentage[i]);

            setTextSizeAndPaddingForTextViews(18, 16, textView1, textView2, textView3);
            // Add TextViews to the TableRow
            sextableRow.addView(textView1);
            sextableRow.addView(textView2);
            sextableRow.addView(textView3);

            // Add the TableRow to the TableLayout
            sextableLayout.addView(sextableRow);
        }
        //-------------------End of Table 2---------------------------//

        //-------------------Table 3----------------------------------//
        String[] category = {"Underweight", "Severe Underweight", "Overweight", "Stunted", "Severe Stunted", "Wasted", "Severe Wasted"};
        TableLayout tableLayout3 = view.findViewById(R.id.tableLayout3);
        // Create a header TableRow
        TableRow headerRowCategory = new TableRow(requireContext());
        headerRowCategory.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        // Create the header for the first column ("Age")
        TextView headerCategory = new TextView(requireContext());
        headerCategory.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        headerCategory.setText("Category");
        headerRowCategory.addView(headerCategory);

        // Create the header for the second column ("Total")
        TextView headerTotalCategory = new TextView(requireContext());
        headerTotalCategory.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT, 1f));
        headerTotalCategory.setGravity(Gravity.END); // Align header text to the right
        headerTotalCategory.setText("Total");
        headerRowCategory.addView(headerTotalCategory);

        // Add the header TableRow to the TableLayout
        tableLayout3.addView(headerRowCategory);
        // Create the table dynamically with 2 columns and 6 rows
        for (int i = 0; i < 7; i++) {
            // Create a new TableRow
            TableRow tableRow = new TableRow(requireContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Create the first TextView (first column)
            TextView textView1 = new TextView(requireContext());
            textView1.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Layout weight is set to 1f
            textView1.setText(category[i]);

            // Create the second TextView (second column)
            TextView textView2 = new TextView(requireContext());
            textView2.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Layout weight is set to 1f
            textView2.setGravity(Gravity.END); // Align text to the right
            textView2.setText(Integer.toString(rand.nextInt(upperbound)));


            setTextSizeAndPaddingForTextViews(18, 16, textView1, textView2);
            // Add TextViews to the TableRow
            tableRow.addView(textView1);
            tableRow.addView(textView2);

            // Add the TableRow to the TableLayout
            tableLayout3.addView(tableRow);
        }


        //-------------------End of Table 3--------------------------//
        //for easier config
        setTextSizeAndPaddingForTextViews(18, 18, headerAge, headerTotal, headerSex, headerPercentageSex,
                headerTotalSex, headerCategory, headerTotalCategory);
        return view;
    }



    private void setTextSizeAndPaddingForTextViews(float textSize, int padding, TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            textView.setPadding(padding, padding, padding, padding);
        }
    }
}