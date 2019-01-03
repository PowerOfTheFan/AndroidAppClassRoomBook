package com.example.android.teachingroomreservation.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CompareDay {

    public long differanceDay(String endDate){
        long getDaysDiff;
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date currentDate = new Date();
        Date date1 = null;
        Date date2 = null;

        try {
            String toDay = simpleDateFormat.format(currentDate);

            date1 = simpleDateFormat.parse(toDay);
            date2 = simpleDateFormat.parse(endDate);

            long getDiff = date2.getTime() - date1.getTime();

            return getDaysDiff = getDiff / (24 * 60 * 60 * 1000);

//            System.out.println("Differance between date " + startDate + " and " + endDate + " is " + getDaysDiff + " days.");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
