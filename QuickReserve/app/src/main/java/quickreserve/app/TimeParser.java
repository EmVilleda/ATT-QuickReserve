package quickreserve.app;

/*
*   Class which renders time properly in all the textview, boxes etc
* */

import android.util.Log;

import java.util.Calendar;

/**
 * Created by at892q on 6/18/2014.
 */
public class TimeParser {
    final static String[] monthArray = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    final static String[] dayArray = {"Sunday", "Monday", "Tuesday" , "Wednesday", "Thursday", "Friday", "Saturday"};
    public TimeParser(){
    }

    public static String getDay(int date)
    {
        String stringDate = Integer.toString(date);
        int year = Integer.parseInt(stringDate.substring(0,4));
        int month = Integer.parseInt(stringDate.substring(4,6));
        int day = Integer.parseInt(stringDate.substring(6));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Log.e("date", "index = " + (cal.get(Calendar.DAY_OF_MONTH) + 1) % 7 );
        return dayArray[(cal.get(Calendar.DAY_OF_MONTH) + 1) % 7];
    }

    public static String parseDate(int year, int month, int day){
        String newDate = "";
        if(month < 10){
            newDate = newDate + "0";
        }
        newDate = newDate + month + "/";
        if(day<10){
            newDate = newDate + "0";
        }
        return (newDate + day + "/" + year);
    }

    public static String parseDate(int date){
        String stringDate = Integer.toString(date);
        String newDate = monthArray[Integer.parseInt(stringDate.substring(4,6)) - 1];
        return newDate + " " + stringDate.substring(6) + ", " + stringDate.substring(0,4);
    }

    public static String parseDateFormat(int date){
        String stringDate = Integer.toString(date);
        return stringDate.substring(4,6) +"/" + stringDate.substring(6) + "/" + stringDate.substring(0,4);
    }

    public static int parseDate(String date){
        date = date.replace("/", "");
        String month = date.substring(0,2);
        String day = date.substring(2,4);
        String year = date.substring(4);

        return Integer.parseInt("" + year + month + day);
    }

    public static String parseTime(int startTime, int endTime){
        return parseTime(startTime) + " - " + parseTime(endTime);
    }

    public static String parseTime(int time){
        if(time<1000&& time >= 100){
            return Integer.toString(time).substring(0,1)+ ":" + Integer.toString(time).substring(1) +" AM";
        }
        else {
            if (time < 1200) {
                return Integer.toString(time).substring(0, 2) + ":" + Integer.toString(time).substring(2) + " AM";
            }
            else if (time >= 1300) {
                int pmTime = time % 1200;

                if (pmTime < 1000) {
                    return Integer.toString(pmTime).substring(0, 1) + ":" + Integer.toString(pmTime).substring(1) + " PM";
                } else {
                    return Integer.toString(pmTime).substring(0, 2) + ":" + Integer.toString(pmTime).substring(2) + " PM";
                }
            }
            else {
                int pmTime = time;
                String ampm = " PM";
                if (pmTime < 100) {
                    pmTime = pmTime + 1200;
                    ampm = " AM";
                }
                return Integer.toString(pmTime).substring(0, 2) + ":" + Integer.toString(pmTime).substring(2) + ampm;
            }
        }
    }

    public static String parseCalendarTime(int hourOfDay, int minute){
        StringBuilder sb = new StringBuilder();
        if(hourOfDay%12 < 10)
        {
            if(hourOfDay == 12 || hourOfDay == 0)
            {
                sb.append(12).append(":");
            }
            else
                sb.append("0").append(hourOfDay%12).append(":");

        }
        else
            sb.append(hourOfDay%12).append(":");

        if(minute < 10)
        {
            sb.append("0").append(minute + " ");
        }
        else
            sb.append(minute + " ");

        if (hourOfDay%24 >= 12 )
        {
            sb.append("PM");

        }
        else
            sb.append("AM");
        return sb.toString();
    }

    public static int parseTime(String time){
        time = time.replace(":", "");
        time = time.replace(" ", "");
        if (time.substring(time.length() - 2).equals("PM")) {
            time = time.replace("PM", "");
            if(time.substring(0,2).equals("12")){
                return Integer.parseInt(time);
            }
            return Integer.parseInt(time) + 1200;
        }
        else {
            time = time.replace("AM", "");
            return Integer.parseInt(time);
        }
    }
}
