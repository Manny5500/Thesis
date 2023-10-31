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

    public ArrayList<String> CalculateMalnourished(Context context, int age, double weight, double height, String sex){
        ArrayList<String> status = new ArrayList<>();
        ArrayList<String> statusdb = new ArrayList<>();

        if(sex.equals("Male") && age<60 && age>= 0){
            WFA_Boys wfa = new WFA_Boys();
            if(!wfa.WFA_Boys_M(age,weight).equals("")){
                status.add(wfa.WFA_Boys_M(age, weight));
            }
            if(age>=0 && age<24)
            {
                LFA_Boys lfaBoys = new LFA_Boys();
                if(!lfaBoys.LFA_Boys_M(height,age).equals("")){
                    status.add(lfaBoys.LFA_Boys_M(height, age));
                }

                WFL_Boys wflBoys = new WFL_Boys();
                if(!wflBoys.WFL_Boys_M(weight,height).equals("")){
                    status.add(wflBoys.WFL_Boys_M(weight, height));
                }

            } else if (age>= 24 && age <60) {
                HFA_Boys hfaBoys = new HFA_Boys();
                if(!hfaBoys.HFA_Boys_M(height, age).equals("")){
                    status.add(hfaBoys.HFA_Boys_M(height, age));
                }

                WFH_Boys wfhBoys = new WFH_Boys();
                if(!wfhBoys.WFH_Boys_M(weight, height).equals("")){
                    status.add(wfhBoys.WFH_Boys_M(weight, height));
                }
            }else{
                Toast.makeText(context, "Invalid age", Toast.LENGTH_SHORT).show();
            }
            if(status.isEmpty()){
                status.add("Normal");
            }
            statusdb = showDialogMalnourished(context, status);

        } else if (sex.equals("Female") && age<60 && age>=0) {
            WFA_Girls wfag = new WFA_Girls();
            if(!wfag.WFA_Girls_M(age,weight).equals("")){
                status.add(wfag.WFA_Girls_M(age, weight));
            }
            if(age>=0 && age<24)
            {
                LFA_Girls lfaGirls = new LFA_Girls();
                if(!lfaGirls.LFA_Girls_M(height,age).equals("")){
                    status.add(lfaGirls.LFA_Girls_M(height, age));
                }
                WFL_Girls wflGirls = new WFL_Girls();
                if(!wflGirls.WFL_Girls_M(weight, age).equals("")){
                    status.add(wflGirls.WFL_Girls_M(weight, height));
                }

            } else if (age>= 24 && age <60) {
                HFA_Girls  hfaGirls = new HFA_Girls();
                if(!hfaGirls.HFA_Girls_M(height,age).equals("")){
                    status.add(hfaGirls.HFA_Girls_M(height, age));
                }
                WFH_Girls wfhGirls = new WFH_Girls();
                if(!wfhGirls.WFH_Girls_M(weight, height).equals("")){
                    status.add(wfhGirls.WFH_Girls_M(weight, height));
                }
            }else{
                Toast.makeText(context, "Invalid age", Toast.LENGTH_SHORT).show();
            }

            if(status.isEmpty()){
                status.add("Normal");
            }
            statusdb = showDialogMalnourished(context, status);

        } else {
            Toast.makeText(context, "Invalid ages", Toast.LENGTH_SHORT).show();
        }

        return statusdb;
    }



    public static ArrayList<String> showDialogMalnourished(Context context, ArrayList<String> status){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LinkedHashSet<String> setWithoutDuplicates = new LinkedHashSet<>(status);
        ArrayList<String> listWithoutDuplicates = new ArrayList<>(setWithoutDuplicates);

        String message = "";
        for(String element: listWithoutDuplicates){
            message = message + "\t" + element + "\n";
        }
        builder.setTitle("Child Status")
                .setMessage(message);

        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();

        return listWithoutDuplicates;
    };

    public static boolean validateForm(String childFirstName, String childMiddleName, String childLastName,
                                 String parentFirstName, String parentMiddleName, String parentLastName,
                                 String gmail, String houseNumber, String bdate, String expectedDate,
                                 String sexAC, String belongAC, String barangayAC, String height, String weight, int monthdiff, Context context) {

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

    public static Set<String> Recommendations(ArrayList<String> statusList, int age){

        Set<String> recommendationSet = new HashSet<>();
        StringBuilder recommendation = new StringBuilder();
        ArrayList<String> statuses = new ArrayList<>();
        //ArrayList<String> recommendationSet = new ArrayList<>();
        statuses.add("Severe Underweight");
        statuses.add("Severe Wasted");
        String[] recommendations = {
                "Exclusive breastfeeding", "Refer to pediatrician",
                "Regularly monitor your child's growth", "Immediate Medical Care",
                "Go to the nearest medical facility", "Emergency",
                "Portion control", "Give nutritious food",
                "Frequent, Balanced Meal", "Refer to pediatrician if necessary",
                "Encourage balanced diet", "Control the food portion",
                "Go to the nearest hospital", "Encourage physical activity"
        };

        for(String status:statusList){
            Boolean isUnderweight = status.equals("Underweight");
            Boolean isSevereUnderweight = status.equals("Severe Underweight");
            Boolean isWasted = status.equals("Wasted");
            Boolean isSevereWasted = status.equals("Severe Wasted");
            Boolean isStunted = status.equals("Stunted");
            Boolean isSevereStunted = status.equals("Severe Stunted");
            Boolean isOverweight = status.equals("Overweight");
            if(age<=6){
                recommendationSet.add(recommendations[0]);

                if(isUnderweight){
                    recommendationSet.add(recommendations[1]);
                    recommendationSet.add(recommendations[2]);
                }
                if(isSevereUnderweight||isStunted||isWasted){
                    recommendationSet.add(recommendations[3]);
                    recommendationSet.add(recommendations[4]);
                }
                if(isSevereStunted||isSevereWasted){
                        recommendationSet.add(recommendations[5]);
                        recommendationSet.add(recommendations[3]);
                        recommendationSet.add(recommendations[4]);
                }
                if(isOverweight){
                    recommendationSet.add(recommendations[1]);
                }
            }

            if(age>6 && age<=23){
                if(isUnderweight){
                    recommendationSet.add(recommendations[1]);
                }
                if(isOverweight){
                    recommendationSet.add(recommendations[6]);
                }
                if(isStunted){
                    recommendationSet.add(recommendations[1]);
                    recommendationSet.add(recommendations[7]);
                }
                if(isSevereUnderweight||isSevereWasted||isSevereStunted||isWasted){
                    recommendationSet.add(recommendations[5]);
                    recommendationSet.add(recommendations[3]);
                    recommendationSet.add(recommendations[4]);
                }
            }
            if(age>=23 && age<=59){
                if(isUnderweight){
                    recommendationSet.add(recommendations[7]);
                    recommendationSet.add(recommendations[8]);
                    recommendationSet.add(recommendations[9]);
                }
                if(isOverweight){
                    recommendationSet.add(recommendations[10]);
                    recommendationSet.add(recommendations[11]);
                    recommendationSet.add(recommendations[13]);
                }
                if(isStunted){
                    recommendationSet.add(recommendations[7]);
                    recommendationSet.add(recommendations[1]);
                }
                if(isSevereUnderweight||isSevereWasted||isSevereStunted||isWasted){
                    recommendationSet.add(recommendations[5]);
                    recommendationSet.add(recommendations[3]);
                    recommendationSet.add(recommendations[4]);
                }
            }

        }
        return recommendationSet;
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
