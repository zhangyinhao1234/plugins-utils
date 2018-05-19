package com.binpo.plugin.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public synchronized static String formatLongDate(Date date) {
        return sdf.format(date);
    }
}
