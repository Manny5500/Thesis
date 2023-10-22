package com.example.myapplication;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateParser {

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
}
