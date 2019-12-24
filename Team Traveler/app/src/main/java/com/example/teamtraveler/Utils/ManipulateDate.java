package com.example.teamtraveler.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ManipulateDate {

    public static String getDateFormatFrench(String date){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(Date.parse(date));
    }
    public static int[] parseDate(String dateStr){
        int[] res = new int[3];
        String[] date=dateStr.split("/");
        if(date.length==3){
            res[0]=Integer.parseInt(date[0]);
            res[1]=Integer.parseInt(date[1])-1;
            res[2]=Integer.parseInt(date[2]);
        }
        else{
            Calendar cldr = Calendar.getInstance();
            res[0]= cldr.get(Calendar.DAY_OF_MONTH);
            res[1]=cldr.get(Calendar.MONTH);
            res[2]=cldr.get(Calendar.YEAR);
        }
        return res;
    }

    public static int compareDate(Date d1, Date d2) {
        if (d1.getYear() != d2.getYear())
            return d1.getYear() - d2.getYear();
        if (d1.getMonth() != d2.getMonth())
            return d1.getMonth() - d2.getMonth();
        return d1.getDate() - d2.getDate();
    }

    public static Date strTodDate(String dateStr)  {

        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getTimeOfDate(String dateStr){
        int startDate[]=ManipulateDate.parseDate(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.set(startDate[2],startDate[1],startDate[0]);
        long timeStartDate=calendar.getTimeInMillis();
        return timeStartDate;
    }

}
