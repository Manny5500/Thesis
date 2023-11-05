package com.example.myapplication;

import android.view.View;

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

import java.util.ArrayList;

public class ChartMaker {
    public static BarChart createBarChart(View view, int chartId, ArrayList<BarEntry> entries,
                                          String[] labels, int[] colors, String description) {
        BarChart barChart = view.findViewById(chartId);

        BarDataSet barDataSet = new BarDataSet(entries, "Bar Chart");
        barDataSet.setColors(colors);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.setFitBars(true);
        barChart.getDescription().setText(description);
        barChart.animateY(2000);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftYAxis = barChart.getAxisLeft();
        YAxis rightYAxis = barChart.getAxisRight();
        leftYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMinimum(0f);

        barChart.getLegend().setEnabled(false);
        barChart.invalidate();

        return barChart;
    }

    public static PieChart createPieChart(View view, int chartId, int[] colors, int count_Male, int count_Female, ArrayList<Child> childrenList){
        PieChart pieChart = view.findViewById(chartId);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry(count_Male, String.format( "%.2f", ((double)count_Male/childrenList.size()) * 100) + "% - Male"));
        pieEntries.add(new PieEntry(count_Female, String.format("%.2f", ((double)count_Female/childrenList.size()) * 100) + "% - Female"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(colors[0], colors[1]);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false); // Enable drawing values (percentage)
        pieChart.setData(pieData);

        // Customize chart appearance
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(colors[2]);
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
        return pieChart;
    }
}
