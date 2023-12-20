package com.example.myapplication;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ChartMaker {
    public static BarChart createBarChart(View view, int chartId, ArrayList<BarEntry> entries,
                                          String[] labels, int[] colors, String description) {
        BarChart barChart = view.findViewById(chartId);

        BarDataSet barDataSet = new BarDataSet(entries, "Bar Chart");
        barDataSet.setColors(colors);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.setFitBars(true);
        barChart.getDescription().setText("");
        barChart.animateY(2000);
        barChart.setHighlightPerTapEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftYAxis = barChart.getAxisLeft();
        YAxis rightYAxis = barChart.getAxisRight();
        leftYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMinimum(0f);

        xAxis.setDrawGridLines(false);

        integerValues(barDataSet);
        integerValues(barChart);

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

    public static LineChart createLineChart(View view, int chartId, Map<String, Integer> map, Map<String, Integer> map2, String periodType,
                                            String switchStatus){

        LineChart lineChart = view.findViewById(chartId);
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();

        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<String> labels2 = new ArrayList<>();
        int i = 0;
        for (String key : map.keySet()) {
            Integer value = map.get(key);
            entries.add(new Entry(i, value));
            labels.add(i,key.substring(5, key.length()));
            i++;
        }
        int j = 0;

        if(switchStatus.equals("off") && periodType.equals("month")){
            for (String key : map2.keySet()) {
                if (j < i) {
                    Integer value = map2.get(key);
                    entries2.add(new Entry(j, value));
                    labels2.add(j, key.substring(5, key.length()));
                    j++;
                } else {
                    break;
                }
            }
        } else {
            for (String key : map2.keySet()) {
                    Integer value = map2.get(key);
                    entries2.add(new Entry(j, value));
                    labels2.add(j, key.substring(5, key.length()));
                    if(j>=i && periodType.equals("month")){
                        entries.add(new Entry(j, 0));
                        if(labels.get(0).length()==5){
                            labels.add(j, labels.get(0).substring(0,3) + key.substring(8, key.length()));
                        }
                    }
                    j++;
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Current " + periodType);
        LineDataSet dataSet2 = new LineDataSet(entries2, "Previous " + periodType);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        integerValues(dataSet);
        integerValues(dataSet2);
        integerValues(lineChart);

        descriptionRemover(lineChart);


        lineChart.animateX(1000);
        customizeChartLine(dataSet, Color.parseColor("#0000FF"));
        customizeChartLine(dataSet2, Color.parseColor("#00FF00"));

        xAxis.setDrawGridLines(false);


        LineData lineData = new LineData(dataSet2, dataSet);
        lineChart.setData(lineData);


        return lineChart;
    }

    private static void customizeChartLine(LineDataSet dataSet, int color){
        dataSet.setColor(color);
        dataSet.setLineWidth(3f);
        dataSet.setValueTextColor(color);
        dataSet.setCircleColor(color);
        dataSet.setCircleRadius(1f);
    }

    private static void integerValues(LineDataSet dataSet){
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Format the value as an integer
                return String.valueOf((int) value);
            }
        });

    }

    private static void integerValues(BarDataSet dataSet){
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Format the value as an integer
                return String.valueOf((int) value);
            }
        });

    }
    private static void integerValues(LineChart lineChart){
        lineChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Format the value as an integer
                return String.valueOf((int) value);
            }
        });
        lineChart.getAxisRight().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Format the value as an integer
                return String.valueOf((int) value);
            }
        });

    }

    private static void integerValues(BarChart barChart){
        barChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Format the value as an integer
                return String.valueOf((int) value);
            }
        });
        barChart.getAxisRight().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Format the value as an integer
                return String.valueOf((int) value);
            }
        });

    }

    private static  <T extends Chart<?>> void descriptionRemover (T chart) {
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
    }


}
