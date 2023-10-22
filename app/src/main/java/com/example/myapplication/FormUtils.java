package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

public class FormUtils {

    public static void setAdapter(String[] source, MaterialAutoCompleteTextView editText, Context context) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, source);
        editText.setAdapter(adapter);
    }

    public static void dateClicked(final TextInputEditText editText, Context context) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editText, context);
            }
        });
    }

    private static void showDatePickerDialog(final TextInputEditText editText, Context context) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                editText.setText(selectedDate);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public static void CalculateMalnourished(Context context, int age, double weight, double height, String sex){
        ArrayList<String> status = new ArrayList<>();
        if(sex.equals("Male") && age<60 && age>= 0){
            WFA_Boys wfa = new WFA_Boys();
            status.add(wfa.WFA_Boys_M(age, weight));
            if(age>=0 && age<24)
            {
                LFA_Boys lfaBoys = new LFA_Boys();
                status.add(lfaBoys.LFA_Boys_M(height, age));

                WFL_Boys wflBoys = new WFL_Boys();
                status.add(wflBoys.WFL_Boys_M(weight, height));

            } else if (age>= 24 && age <60) {
                HFA_Boys hfaBoys = new HFA_Boys();
                status.add(hfaBoys.HFA_Boys_M(height, age));

                WFH_Boys wfhBoys = new WFH_Boys();
                status.add(wfhBoys.WFH_Boys_M(weight, height));
            }else{
                Toast.makeText(context, "Invalid age", Toast.LENGTH_SHORT).show();
            }
            showDialogMalnourished(context, status);


        } else if (sex.equals("Female") && age<60 && age>=0) {
            WFA_Girls wfag = new WFA_Girls();
            status.add(wfag.WFA_Girls_M(age, weight));
            if(age>=0 && age<24)
            {
                LFA_Girls lfaGirls = new LFA_Girls();
                status.add(lfaGirls.LFA_Girls_M(height, age));

                WFL_Girls wflGirls = new WFL_Girls();
                status.add(wflGirls.WFL_Girls_M(weight, height));

            } else if (age>= 24 && age <60) {
                HFA_Girls  hfaGirls = new HFA_Girls();
                status.add(hfaGirls.HFA_Girls_M(height, age));
                WFH_Girls wfhGirls = new WFH_Girls();
                status.add(wfhGirls.WFH_Girls_M(weight, height));
            }else{
                Toast.makeText(context, "Invalid age", Toast.LENGTH_SHORT).show();
            }

            showDialogMalnourished(context, status);

        } else {
            Toast.makeText(context, "Invalid ages", Toast.LENGTH_SHORT).show();
        }


    }


    public static void showDialogMalnourished(Context context, ArrayList<String> status){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LinkedHashSet<String> setWithoutDuplicates = new LinkedHashSet<>(status);
        List<String> listWithoutDuplicates = new ArrayList<>(setWithoutDuplicates);

        String message = "";
        for(String element: listWithoutDuplicates){
            message = message + "\t" + element + "\n";
        }
        builder.setTitle("Child Status")
                .setMessage(message);

        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    };


    public static boolean validateForm(String childFirstName, String childMiddleName, String childLastName,
                                 String parentFirstName, String parentMiddleName, String parentLastName,
                                 String gmail, String houseNumber, String bdate, String expectedDate,
                                 String sexAC, String belongAC, String barangayAC, String height, String weight, Context context) {

        if (childFirstName.isEmpty() || childMiddleName.isEmpty() || childLastName.isEmpty() ||
                parentFirstName.isEmpty() || parentMiddleName.isEmpty() || parentLastName.isEmpty() ||
                gmail.isEmpty() || houseNumber.isEmpty() || bdate.isEmpty() || expectedDate.isEmpty() ||
                sexAC.isEmpty() || belongAC.isEmpty() || barangayAC.isEmpty() || height.isEmpty() || weight.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern = "^[a-zA-Z0-9._-]+@gmail\\.com$";
        if (!gmail.matches(emailPattern)) {
            // Email format is invalid
            Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        //String firstnamePattern = "[a-zA-Z]+([.\\s]?[a-zA-Z]+)*";
        String namePattern = "[a-zA-Z]+([.\\s]?[a-zA-Z]+)?([\\s]?[a-zA-Z]+)?";
        if (!childFirstName.matches(namePattern) || !parentFirstName.matches(namePattern)) {
            Toast.makeText(context, "Invalid First Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!childMiddleName.matches(namePattern) || !parentMiddleName.matches(namePattern)) {
            Toast.makeText(context, "Invalid Middle Name", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!childLastName.matches(namePattern) || !parentLastName.matches(namePattern)) {
            Toast.makeText(context, "Invalid Last Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        String datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";
        if (!expectedDate.matches(datePattern) || !bdate.matches(datePattern)) {
            Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = null;

        try {
            parsedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedDate;
    }

    public int calculateMonthsDifference(Date givenDate) {
        if (givenDate != null) {
            Calendar currentCalendar = Calendar.getInstance();
            Calendar givenCalendar = Calendar.getInstance();
            givenCalendar.setTime(givenDate);
            int years = currentCalendar.get(Calendar.YEAR) - givenCalendar.get(Calendar.YEAR);
            int months = currentCalendar.get(Calendar.MONTH) - givenCalendar.get(Calendar.MONTH);

            return years * 12 + months;
        }
        return 0;
    }

    public static double roundToNearestEnding(double number) {
        if (number % 1 == 0 || number % 1 == 0.5) {
            return number;
        } else {
            double roundedDown = Math.floor(number);
            double roundedUp = Math.ceil(number);

            if (number - roundedDown < roundedUp - number) {
                return roundedDown;
            } else {
                return roundedUp;
            }
        }
    }

    public static int findElementPosition(double[] array, double target) {
        if(target<45 || target>120){
            if (target<45){
                return 0;
            }else if(target>120){
                return array.length-1;
            }

        }else {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == target) {
                    return i;
                }
            }
        }
        return -1;
    }
}
