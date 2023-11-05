package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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


    public static boolean validateForm(String childFirstName, String childMiddleName, String childLastName,
                                       String parentFirstName, String parentMiddleName, String parentLastName,
                                       String gmail, String houseNumber, String bdate, String expectedDate,
                                       String sexAC, String belongAC,  String height, String weight, int monthdiff, Context context) {

        if (childFirstName.isEmpty() || childMiddleName.isEmpty() || childLastName.isEmpty() ||
                parentFirstName.isEmpty() || parentMiddleName.isEmpty() || parentLastName.isEmpty() ||
                gmail.isEmpty() || houseNumber.isEmpty() || bdate.isEmpty() || expectedDate.isEmpty() ||
                sexAC.isEmpty() || belongAC.isEmpty() || height.isEmpty() || weight.isEmpty()) {
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

        String datePattern = "^([1-9]|0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";
        if (!expectedDate.matches(datePattern) || !bdate.matches(datePattern)) {
            Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(monthdiff<0 || monthdiff>59){
            Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    public static boolean validateForm_R(String childFirstName, String childMiddleName, String childLastName,
                                       String gmail,  String bdate, String sexAC, String barangayAC,
                                         String password, String cpassword, String user, String contact, Context context) {

        if (childFirstName.isEmpty() || childMiddleName.isEmpty() || childLastName.isEmpty() ||
                gmail.isEmpty() || bdate.isEmpty() || sexAC.isEmpty() || barangayAC.isEmpty() ||
                password.isEmpty() || cpassword.isEmpty() || user.isEmpty() || contact.isEmpty()) {
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
        if (!childFirstName.matches(namePattern) ) {
            Toast.makeText(context, "Invalid First Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!childMiddleName.matches(namePattern)) {
            Toast.makeText(context, "Invalid Middle Name", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!childLastName.matches(namePattern)) {
            Toast.makeText(context, "Invalid Last Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        String datePattern = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01]|[1-9])/(\\d{4})$";
        if ( !bdate.matches(datePattern)) {
            Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show();
            return false;
        }

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if(!password.matches(passwordPattern) || !cpassword.matches(passwordPattern)){
            Toast.makeText(context, "Use strong password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(cpassword)){
            Toast.makeText(context, "Password did not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        String contactNumberPattern = "^09\\d{9}$";

        if(!contact.matches(contactNumberPattern)){
            Toast.makeText(context, "Invalid Contact", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean validateForm_UM(String childFirstName, String childMiddleName, String childLastName,
                                         String contact, String sexAC, String barangayAC,
                                          String bdate, Context context) {

        if (childFirstName.isEmpty() || childMiddleName.isEmpty() || childLastName.isEmpty() ||
                bdate.isEmpty() || sexAC.isEmpty() || barangayAC.isEmpty() || contact.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        //String firstnamePattern = "[a-zA-Z]+([.\\s]?[a-zA-Z]+)*";
        String namePattern = "[a-zA-Z]+([.\\s]?[a-zA-Z]+)?([\\s]?[a-zA-Z]+)?";
        if (!childFirstName.matches(namePattern) ) {
            Toast.makeText(context, "Invalid First Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!childMiddleName.matches(namePattern)) {
            Toast.makeText(context, "Invalid Middle Name", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!childLastName.matches(namePattern)) {
            Toast.makeText(context, "Invalid Last Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        String datePattern = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01]|[1-9])/(\\d{4})$";
        if ( !bdate.matches(datePattern)) {
            Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show();
            return false;
        }

        String contactNumberPattern = "^09\\d{9}$";

        if(!contact.matches(contactNumberPattern)){
            Toast.makeText(context, "Invalid Contact", Toast.LENGTH_SHORT).show();
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
    public static int calculateDaysDifference(Date givenDate) {
        if (givenDate != null) {
            Calendar currentCalendar = Calendar.getInstance();
            Calendar givenCalendar = Calendar.getInstance();
            givenCalendar.setTime(givenDate);

            long currentTimeInMillis = currentCalendar.getTimeInMillis();
            long givenTimeInMillis = givenCalendar.getTimeInMillis();

            long differenceInMillis = currentTimeInMillis - givenTimeInMillis;
            int daysDifference = (int) (differenceInMillis / (24 * 60 * 60 * 1000));

            return daysDifference;
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

    public String MotoNo(String monthVal){
        String motono = "";
        if(monthVal.equals("Jan")){
            motono = "01";
        } else if (monthVal.equals("Feb")) {
            motono = "02";
        } else if(monthVal.equals("Mar")){
            motono = "03";
        } else if(monthVal.equals("Apr")){
            motono = "04";
        } else if (monthVal.equals("May")) {
            motono = "05";
        } else if (monthVal.equals("Jun")){
            motono = "06";
        } else if (monthVal.equals("Jul")){
            motono = "07";
        } else if (monthVal.equals("Aug")){
            motono = "08";
        } else if (monthVal.equals("Sep")){
            motono = "09";
        } else if (monthVal.equals("Oct")){
            motono = "10";
        } else if (monthVal.equals("Nov")){
            motono = "11";
        } else if (monthVal.equals("Dec")) {
            motono = "12";
        }
        return motono;
    }
    public static void redirectToRoleSpecificActivity(FirebaseUser user, FirebaseFirestore db, Context context) {
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userDocRef = db.collection("users").document(userId);
            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String userRole = documentSnapshot.getString("user");
                    Intent intent = null;
                    if ("admin".equals(userRole)) {
                        intent = new Intent(context, AdminActivity.class);
                    } else if ("personnel".equals(userRole)) {
                        intent = new Intent(context, PersonnelActivity.class);
                    } else if ("parent".equals(userRole)) {
                        intent = new Intent(context, ParentActivity.class);
                    }

                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                } else {
                    // Document doesn't exist, handle the error or redirect to an appropriate activity.
                }
            });
        }
    }
}
