package com.dede.dedegame.presentation.home.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    private static final SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    private static final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    public static String format(Date date){
        return format1.format(date);
    }
    public static String format2(Date date){
        return format2.format(date);
    }
}
