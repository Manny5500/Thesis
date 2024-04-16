package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormatSymbols;

public class PMEdit extends AppCompatActivity {
    TextView title, name, age, status;
    TextInputEditText height, weight;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmedit);

        title = findViewById(R.id.title);
        name = findViewById(R.id.Name);
        age = findViewById(R.id.Age);
        status = findViewById(R.id.Status);
        height = findViewById(R.id.textHeight);
        weight = findViewById(R.id.textWeight);
        save = findViewById(R.id.btnEdit);

        setInitialValue();

    }
    private void setInitialValue(){
        String statusList = "Status : \n";
        for(int i=0; i<App.childH.getStatusdb().size(); i++){
            if(i==App.childH.getStatusdb().size()-1){
                statusList = statusList + "\t" + App.childH.getStatusdb().get(i);
            }else{
                statusList = statusList + "\t" + App.childH.getStatusdb().get(i) + "\n";
            }
        }

        //for converting the date
        String dateRaw = App.childH.getId();
        int month = Integer.parseInt(dateRaw.substring(4, 6));
        String monthName = new DateFormatSymbols().getMonths()[month - 1];
        String dateVal = monthName + " " + dateRaw.substring(6, 8) + ", " + dateRaw.substring(0, 4);

        name.setText("Name: " + App.child.getChildFirstName() + " " + App.child.getChildLastName());
        title.setText(dateVal);
        status.setText(statusList);
        height.setText(String.valueOf(App.childH.getHeight()));
        weight.setText(String.valueOf(App.childH.getWeight()));

    }
}