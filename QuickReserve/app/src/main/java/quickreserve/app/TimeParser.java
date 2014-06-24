package quickreserve.app;

import android.content.Context;
import android.content.res.Resources;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

/**
 * Created by at892q on 6/18/2014.
 */
public class TimeParser {
    final static String[] monthArray = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    public TimeParser(){
    }

    public static String parseDate(int date){;
        String stringDate = Integer.toString(date);
        String newDate = monthArray[Integer.parseInt(stringDate.substring(4,6)) - 1];
        return newDate + " " + stringDate.substring(6) + ", " + stringDate.substring(0,4);
    }

    public static String parseTime(int startTime, int endTime){
        String newTime ="";
        if(startTime<1000){
            newTime = Integer.toString(startTime).substring(0,1)+ ":" + Integer.toString(startTime).substring(1);
        }
        else{
            newTime = Integer.toString(startTime).substring(0,2) + ":" + Integer.toString(startTime).substring(2);
        }
        if(endTime<1000){
            newTime = newTime + " - " + Integer.toString(endTime).substring(0,1)+ ":" + Integer.toString(endTime).substring(1);
        }
        else{
            newTime = newTime + " - " + Integer.toString(endTime).substring(0,2) + ":" + Integer.toString(endTime).substring(2);
        }

        return newTime;
    }
}
