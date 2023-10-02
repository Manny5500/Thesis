package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class fradAddData extends Fragment {
    View view;
    Spinner spinnerBarangay, spinnerSex, spinnerSitio;
    CheckBox checkBoxYes, checkBoxNo;

    EditText bdate, expectedDate;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frad_add_data, container, false);


        String[] barangay = getResources().getStringArray(R.array.barangay);
        String[] sex = getResources().getStringArray(R.array.sex);

        spinnerBarangay = view.findViewById(R.id.spinnerBarangay);
        spinnerSex = view.findViewById(R.id.spinnerSex);
        spinnerSitio = view.findViewById(R.id.spinnerSitio);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_dropdown_item, barangay);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerBarangay.setAdapter(adapter);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), R.layout.spinner_dropdown_item, sex);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSex.setAdapter(adapter2);

        checkBoxYes = view.findViewById(R.id.checkYes);
        checkBoxNo = view.findViewById(R.id.checkNo);

        bdate = view.findViewById(R.id.textBirthdate);
        expectedDate = view.findViewById(R.id.textExpectedDate);

        spinnerBarangay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = (String) parentView.getItemAtPosition(position);
                // Do something with the selected category
                // Toast.makeText(requireContext(), selectedCategory,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });


        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = (String) parentView.getItemAtPosition(position);
                // Do something with the selected category
                //Toast.makeText(requireContext(), selectedCategory,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });


        checkBoxYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Disable checkBoxNo if checkBoxYes is checked, enable it otherwise
                checkBoxNo.setEnabled(!isChecked);

                // You can also uncheck checkBoxNo if checkBox1 is checked
                if (isChecked) {
                    checkBoxNo.setChecked(false);
                }
            }
        });

        checkBoxNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Disable checkBoxYes if checkBoxNo is checked, enable it otherwise
                checkBoxYes.setEnabled(!isChecked);

                // You can also uncheck checkBoxYes if checkBoxNo is checked
                if (isChecked) {
                    checkBoxYes.setChecked(false);
                }
            }
        });

        bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(bdate);
            }
        });

        expectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(expectedDate);
            }
        });

        return view;
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Handle the selected date here
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                editText.setText(selectedDate);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

}