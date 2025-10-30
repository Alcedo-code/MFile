package com.angel.minio.util.utils;


import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author yangb
 * @Date 2022/5/3 19:00
 * @Version 1.0
 * 时间工具类
 */
public class DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String HHMMSS = "HHmmss";

    public static String SS = "ss";

    public static String SSS = "ssSSS";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    /**
     * 获取当前时分秒
     *
     * @return
     */
    public static final String dateTimeNowShort() {
        return dateTimeNow(SS);
    }

    /**
     * 获取当前毫秒
     *
     * @return
     */
    public static final String dateTimeNowSss() {
        return dateTimeNow(SSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }


    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final String parseDateToStr(final String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
            Date date_new = sdf.parse(date);
            return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(date_new);
        } catch (Exception ex) {
            return "";
        }
    }

    public static final Date parseDate(final String date) {
        try {
            SimpleDateFormat format;
            int lenth = date.length();
            if (lenth == 20) {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            }
            else if (lenth == 24) {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            }
            else if (lenth == 23) {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
            } else {
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            }

            Date date_new = format.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date_new);
            calendar.add(Calendar.HOUR_OF_DAY, 8);
            return calendar.getTime();
        } catch (Exception ex) {
            return getNowDate();
        }
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        return day + "天" + hour + "小时" + min + "分钟";
    }

    public static String dateDiff(String startTime, String endTime) throws Exception {
        SimpleDateFormat sd = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        long nd = 1000 * 24 * 60 * 60; //一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数据
        long nm = 1000 * 60; //一分钟毫秒数
        long ns = 1000;
        long diff;
        //获得两个时间的毫秒时间差异
        diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "时" + min + "分" + sec + "秒";
    }

}
