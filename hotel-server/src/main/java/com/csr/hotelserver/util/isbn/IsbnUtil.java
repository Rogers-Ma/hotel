package com.csr.hotelserver.util.isbn;

public class IsbnUtil {
    public static String getOnumber()
    {
        //得到系统时间的微秒数
        long L= System.currentTimeMillis();
        String l=String.valueOf(L).substring(1,9);
        //得到随机数，截取需要的字符串
        double d=Math.random();
        String s=String.valueOf(d).substring(4,9);
        return (l+s);
    }
}
