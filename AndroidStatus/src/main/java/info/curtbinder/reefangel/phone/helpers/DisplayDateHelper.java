package info.curtbinder.reefangel.phone.helpers;

import android.text.format.DateUtils;

import java.util.Date;


public class DisplayDateHelper {

    public static final long YEAR = DateUtils.YEAR_IN_MILLIS;
    public static final long MONTH = 2628000000L;
    public static final long DAY = DateUtils.DAY_IN_MILLIS;
    public static final long HOUR = DateUtils.HOUR_IN_MILLIS;
    public static final long MINUTE = DateUtils.MINUTE_IN_MILLIS;
    public static final long SECOND = DateUtils.SECOND_IN_MILLIS;


    public static String timespanToString(long time){
        if((time / YEAR) >= 1 ){
            long result = (time / YEAR);
            return formatUpdateTime(result, "year");
        } else if((time / MONTH) >= 1 ){
            long result = (time / MONTH);
            return formatUpdateTime(result, "month");
        } else if((time / DAY) >= 1 ){
            long result = (time / DAY);
            return formatUpdateTime(result, "day");
        } else if((time / HOUR) >= 1 ){
            long result = (time / HOUR);
            return formatUpdateTime(result, "hour");
        } else if((time / MINUTE) >= 1 ){
            long result = (time / MINUTE);
            return formatUpdateTime(result, "minute");
        } else if((time / SECOND) >= 1 ){
            long result = (time / SECOND);
            return formatUpdateTime(result, "second");
        } else {
            return "1 second";
        }
    }

    private static String formatUpdateTime(long result, String time){
        return result + " " + time + (result > 1 ? "s" : "");
    }
}
