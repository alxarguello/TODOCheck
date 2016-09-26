package com.alxarguello.apps.todocheck.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alxarguello on 9/26/16.
 */

public final class Utils {


    //No need for an instance for now
    private Utils() {

    }

    public static String getStringDate(long date) {
        String dt;
        if (date > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date d = new Date(date);
            dt = sdf.format(d);
        }else{
            dt="";
        }
        return dt;
    }

}
