package com.mosi.utils;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {


    public static final long ONE_MINUTE_MILLIONS = 60 * 1000;
    public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
    public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;

    /**
     * 获取时间倒计时
     *
     * @param dateStr "2016-01-06T09:37:04"
     * @return
     */
    public static String getTimeDown(String dateStr) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = sdf.parse(dateStr);
            Date curDate = new Date();

            long durTime = curDate.getTime() - date.getTime();
            int dayStatus = calculateDayStatus(date, curDate);

            if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
                str = "刚刚";
            } else if (durTime < ONE_HOUR_MILLIONS) {
                str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
            } else if (dayStatus == 0) {
                str = durTime / ONE_HOUR_MILLIONS + "小时前";
            } else if (dayStatus == -1) {
                str = "昨天" + DateFormat.format("HH:mm", date);
            } else if (isSameYear(date, curDate) && dayStatus < -1) {
                str = DateFormat.format("MM-dd", date).toString();
            } else {
                str = DateFormat.format("yyyy-MM", date).toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    public static boolean isSameYear(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarYear = tarCalendar.get(Calendar.YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comYear = compareCalendar.get(Calendar.YEAR);

        return tarYear == comYear;
    }

    public static int calculateDayStatus(Date targetTime, Date compareTime) {
        Calendar tarCalendar = Calendar.getInstance();
        tarCalendar.setTime(targetTime);
        int tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR);

        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareTime);
        int comDayOfYear = compareCalendar.get(Calendar.DAY_OF_YEAR);

        return tarDayOfYear - comDayOfYear;
    }

    /**
     * Date - String
     *
     * @return
     */
    public static String toDateMouthStr(Date date, String type) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        try {
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param type : 时间格式
     * @return 当天日期
     */
    public static String toDateSS(String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        Calendar c = Calendar.getInstance();

        c.setTime(new Date());
        Date d = c.getTime();
        String day = format.format(d);
        return day;
    }

    /**
     * @param type : 时间格式
     * @return 返回未来7日数据
     */
    public static String toWeekDateSS(String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        Calendar c = Calendar.getInstance();

        //过去七天
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date d = c.getTime();
        String day = format.format(d);
        return day;
    }

    /**
     * 判断两个时间的前后
     *
     * @param DATE1：开始时间
     * @param DATE2：结束时间
     * @return
     */
    public static boolean compare_date(String DATE1, String DATE2) {
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {//dt1 在dt2前
                return false;
            } else if (dt1.getTime() < dt2.getTime()) {//dt1在dt2后
                return true;
            } else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 获取多少之前的日期
     */
    public static String getDateTime(int day) {

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        calendar1.add(Calendar.DATE, day);
        String days_ago = sdf1.format(calendar1.getTime());
        return days_ago;
    }
}
