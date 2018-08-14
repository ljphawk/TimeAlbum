package com.ljp.time.timealbum.utils;



/*
 *@创建者       L_jp
 *@创建时间     2018/7/6 17:25.
 *@描述         ${TODO}
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class DateUtils {

//    public static int get

    //获取当前的时间戳
    public static String getTimeStamp() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }


    /**
     *
     * @param timeStamp1
     * @param timeStamp2
     * @return true  是否是同一天
     */
    public static boolean isToday(String timeStamp1, String timeStamp2) {

        String sdfTime1 = getSdfTime(timeStamp1, YMDHMS1);
        String sdfTime2 = getSdfTime(timeStamp2, YMDHMS1);


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        try {
            Date date1 = format.parse(sdfTime1);
            Date date2 = format.parse(sdfTime2);

            int i = DateUtils.differentDays(date1, date2);//data2-data1
            if (Math.abs(i) == 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * date2比date1多的天数
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            return day2 - day1;
        }
    }


    public static final String YMDHMS1 = "yyyy-MM-dd HH-mm-ss";
    public static final String YMDHMS2 = "yyyy-MM-dd";
    public static final String YMDHMS3 = "HH:mm:ss";


    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *  sdfForm "yyyy-MM-dd-HH-mm-ss"
     */
    public static String getSdfTime(String timeStamp, String sdfForm) {
        SimpleDateFormat sdr = new SimpleDateFormat(sdfForm);

        return sdr.format(new Date(Long.valueOf(timeStamp + "000")));
    }

    public static String getSdfTime(long timeStamp, String sdfForm) {
        return getSdfTime(timeStamp + "", sdfForm);
    }


    /**
     * 把毫秒转换成：1:20:30这里形式
     * @param timeMs
     * @return
     */
    public static String stringForTime(long timeMs) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;


        long minutes = (totalSeconds / 60) % 60;


        long hours = totalSeconds / 3600;


        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}
