package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChildHAdapter  extends RecyclerView.Adapter<ChildHAdapter.ViewHolder> {
    Context context;
    private List<ChildH> exampleList;

    ChildAdapter.OnItemClickListener onItemClickListener;

    public ChildHAdapter(Context context, ArrayList<ChildH> exampleList){
        this.context = context;
        this.exampleList = exampleList;
    }

    @NonNull
    @Override
    public ChildHAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.progress_item, parent, false);
        return new ChildHAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildHAdapter.ViewHolder holder, int position) {
        String dateRaw = "" + exampleList.get(position).getId();
        int month = Integer.parseInt(dateRaw.substring(4, 6));
        String monthName = new DateFormatSymbols().getMonths()[month - 1];

        String dateVal = monthName + " " + dateRaw.substring(6, 8) + ", " + dateRaw.substring(0, 4);
        String heightVal = "" + exampleList.get(position).getHeight();
        String weightVal = "" + exampleList.get(position).getWeight();

        double heightDifference;
        double weightDifference;
        if(position+1<exampleList.size()){
            heightDifference = exampleList.get(position).getHeight() - exampleList.get(position+1).getHeight();
            weightDifference = exampleList.get(position).getWeight() - exampleList.get(position+1).getWeight();
            holder.progressHeight.setText("Height: " + heightVal + " " + heightDifference);
            holder.progressWeight.setText("Weight: " + weightVal + " " + weightDifference);
        }else{
            holder.progressHeight.setText("Height: " + heightVal);
            holder.progressWeight.setText("Weight: " + weightVal);
        }
        holder.progressDate.setText("Date: " + dateVal);
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView progressDate, progressHeight, progressWeight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressDate = itemView.findViewById(R.id.progressDate);
            progressHeight = itemView.findViewById(R.id.progressHeight);
            progressWeight = itemView.findViewById(R.id.progressWeight);
        }
    }


}
