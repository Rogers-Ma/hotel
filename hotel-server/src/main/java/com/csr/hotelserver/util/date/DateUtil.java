package com.csr.hotelserver.util.date;

import java.text.SimpleDateFormat;

public class DateUtil {
    public static java.sql.Date strToSqlDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }

    public static java.util.Date strToUtilDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Integer getDays(String date0, String date1){
        java.util.Date date_0 = strToUtilDate(date0);
        java.util.Date date_1 = strToUtilDate(date1);
        Long time0 = date_0.getTime();
        Long time1 = date_1.getTime();
        Long time3 = (time1-time0)/1000/60/60/24;
        return time3.intValue();
    }
}
