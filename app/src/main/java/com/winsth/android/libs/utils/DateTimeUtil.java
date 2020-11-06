package com.winsth.android.libs.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimeUtil {
    private DateTimeUtil() {
    }

    /**
     * 获取当前日期
     *
     * @return 返回当前日期
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 获取当前日期的前一天
     *
     * @return 返回当前日期的前一天
     */
    public static Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前日期
     *
     * @return 返回当前日期
     */
    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前日期的后一天
     *
     * @return 返回当前日期的后一天
     */
    public static Date getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定月份的第一天
     *
     * @param yyyymm 指定的月份串
     * @return 返回指定月份的第一天
     */
    public static Date getTheFirstDayOfMonth(String yyyymm) {
        String dateStr = yyyymm + "-01 00:00:00";
        return DateTimeUtil.getDateTime(dateStr);
    }

    /**
     * 获取系统月份的第一天
     *
     * @return 返回系统月份的第一天
     */
    public static String getMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取当前季度的第一天
     *
     * @return 返回当前季度第一天
     */
    public static String getCurrentQuarterStartDay() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3)
            c.set(Calendar.MONTH, 0);
        else if (currentMonth >= 4 && currentMonth <= 6)
            c.set(Calendar.MONTH, 3);
        else if (currentMonth >= 7 && currentMonth <= 9)
            c.set(Calendar.MONTH, 4);
        else if (currentMonth >= 10 && currentMonth <= 12)
            c.set(Calendar.MONTH, 9);
        c.set(Calendar.DATE, 1);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(c.getTime());
    }

    /**
     * 获取当年的第一天
     *
     * @return 返回今年第一天
     */
    public static String getYearFirstDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(c.getTime());
    }

    /**
     * 获取两个日其中较小的日期
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 返回两个日期中较小的日期
     */
    public static Date getSmallDate(Date date1, Date date2) {
        return date1.compareTo(date2) < 0 ? date1 : date2;
    }

    /**
     * 获取两个日其中较大的日期
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 返回两个日期中较大的日期
     */
    public static Date getBigDate(Date date1, Date date2) {
        return date1.compareTo(date2) > 0 ? date1 : date2;
    }

    /**
     * 在指定的日期上增加年数
     *
     * @param yearAmount 增加的年数
     * @param date       指定日期
     * @return 返回增加年数后的日期
     */
    public static Date addYear2Date(int yearAmount, Date date) {
        Date newDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, yearAmount);
            newDate = calendar.getTime();
        }
        return newDate;
    }

    /**
     * 在指定的日期上加上月份
     *
     * @param monthAmount 增加的月数
     * @param date        制定的日期
     * @return 返回增加月数后的日期
     */
    public static Date addMonth2Date(int monthAmount, Date date) {
        Date newDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, monthAmount);
            newDate = calendar.getTime();
        }
        return newDate;
    }

    /**
     * 在指定的日期上增加天数
     *
     * @param dayAmount 增加的天数
     * @param date      指定日期
     * @return 返回增加天数后的日期
     */
    public static Date addDay2Date(int dayAmount, Date date) {
        Date newDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, dayAmount);
            newDate = calendar.getTime();
        }
        return newDate;
    }

    /**
     * 根据给定的起始日期、结束日期获得它们之间的所有日期的List对象
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 返回所有日期的List对象
     */
    public static List<Date> getDateList(Date startDate, Date endDate) {
        List<Date> dateList = new ArrayList<Date>();
        int dayAmount = getDiffDays(startDate, endDate);
        Date newDate = null;
        for (int i = 0; i <= dayAmount + 1; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            newDate = calendar.getTime();
            dateList.add(newDate);
        }
        return dateList;
    }

    /**
     * 在指定的日期上增加小时数
     *
     * @param hourAmount 小时数
     * @param date       指定日期
     * @return 返回增加小时数后的日期
     */
    public static Date addHour2Date(int hourAmount, Date date) {
        Date newDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, hourAmount);
            newDate = calendar.getTime();
        }
        return newDate;
    }

    /**
     * 在指定的日期上增加分钟数
     *
     * @param minuteAmount 分钟数
     * @param date         指定日期
     * @return 返回增加分钟数后的日期
     */
    public static Date addMinute2Date(int minuteAmount, Date date) {
        Date newDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, minuteAmount);
            newDate = calendar.getTime();
        }
        return newDate;
    }

    /**
     * 在指定的日期上增加秒数
     *
     * @param secondAmount 秒数
     * @param date         指定日期
     * @return 返回增加分钟数后的日期
     */
    public static Date addSecond2Date(int secondAmount, Date date) {
        Date newDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.SECOND, secondAmount);
            newDate = calendar.getTime();
        }
        return newDate;
    }

    /**
     * 将日期转换成指定格式的字符串
     *
     * @param format 时间表现形式如："yyyy-MM-dd"，"yyyy-MM-dd HH:mm:ss"等
     * @param date   待格式化的日期
     * @return 返回格式化后的日期字符串
     */
    public static String formatDateTime(String format, Date date) {
        return formatDateTime(format, date, "");
    }

    /**
     * 将日期转换成指定格式的字符串
     *
     * @param format     时间表现形式如："yyyy-MM-dd"，"yyyy-MM-dd HH:mm:ss"等
     * @param date       待格式化的日期
     * @param nullString 空日期的替换字符，满足特殊需要
     * @return 返回格式化后的日期字符串
     */
    public static String formatDateTime(String format, Date date, String nullString) {
        String formatStr = nullString;

        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            formatStr = simpleDateFormat.format(date);
        }

        return formatStr;
    }

    /**
     * 将字符串解析成年月类型，如果字符串含有/则按/分割,否则按-分割
     *
     * @param dateYMStr 待解析的字符串
     * @return 返回解析后的日期
     */
    public static Date getDateYM(String dateYMStr) {
        Date date = null;
        try {
            if (dateYMStr != null) {
                String separator = dateYMStr.indexOf('/') > 0 ? "/" : "-";
                DateFormat dateFormat = new SimpleDateFormat("yyyy" + separator + "MM", Locale.getDefault());
                date = dateFormat.parse(dateYMStr);
            }
        } catch (ParseException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "ParseException:" + e.getMessage(), true);
        }
        return date;
    }

    /**
     * 将字符串解析成年月日类型，如果字符串含有/则按/分割,否则按-分割
     *
     * @param dateStr 待解析的字符串
     * @return 返回解析后的日期
     */
    public static Date getDateYMD(String dateStr) {
        Date date = null;
        try {
            if (dateStr != null) {
                String separator = dateStr.indexOf('/') > 0 ? "/" : "-";
                DateFormat dateFormat = new SimpleDateFormat("yyyy" + separator + "MM" + separator + "dd", Locale.getDefault());
                date = dateFormat.parse(dateStr);
            }
        } catch (ParseException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "ParseException:" + e.getMessage(), true);
        }
        return date;
    }

    /**
     * 将字符串解析成日期类型，格式自定
     *
     * @param dateStr   待解析的字符串
     * @param formatStr 日期格式
     * @return 返回解析后的日期
     */
    public static Date getDate(String dateStr, String formatStr) {
        Date date = null;
        try {
            if (dateStr != null) {
                DateFormat dateFormat = new SimpleDateFormat(formatStr, Locale.getDefault());
                date = dateFormat.parse(dateStr);
            }
        } catch (ParseException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "ParseException:" + e.getMessage(), true);
        }
        return date;
    }

    /**
     * 将字符串解析成年月日时分秒日期时间类型，如果字符串含有/则按/分割,否则以-分
     *
     * @param dateTimeStr 待解析的字符串
     * @return 返回解析后的日期
     */
    public static Date getDateTime(String dateTimeStr) {
        Date date = null;
        try {
            String separator = dateTimeStr.indexOf('/') > 0 ? "/" : "-";
            DateFormat dateFormat = new SimpleDateFormat("yyyy" + separator + "MM" + separator + "dd HH:mm:ss", Locale.getDefault());
            date = dateFormat.parse(dateTimeStr);
        } catch (ParseException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "ParseException:" + e.getMessage(), true);
        }
        return date;
    }

    /**
     * 获取传入日期的年份
     *
     * @param date 待解析的日期
     * @return 返回该日期的年份
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取传入日期的月份
     *
     * @param date 待解析的日期
     * @return 返回该日期的月份
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取传入日期月份的日
     *
     * @param date 待解析的日期
     * @return 返回该日期的日
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DATE);
    }

    /**
     * 获取两个日期的月份差
     *
     * @param fromDate 起始日期
     * @param toDate   结束日期
     * @return 返回两个日期的月份差，例1998-4-21~1998-6-21 相差2个月，返回2
     */
    public static int getDiffMonths(Date fromDate, Date toDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(fromDate);
        int fromYear = c.get(Calendar.YEAR);
        int fromMonth = c.get(Calendar.MONTH) + 1;
        c.setTime(toDate);
        int toYear = c.get(Calendar.YEAR);
        int toMonth = c.get(Calendar.MONTH) + 1;
        int monthCount = 0;

        if (toYear == fromYear) {
            monthCount = toMonth - fromMonth;
        } else if (toYear - fromYear == 1) {
            monthCount = 12 - fromMonth + toMonth;
        } else {
            monthCount = 12 - fromMonth + 12 * (toYear - fromYear - 1) + toMonth;
        }
        return monthCount;
    }

    /**
     * 获取两个日期的天数差
     *
     * @param fromDate 起始日期
     * @param toDate   结束日期
     * @return 返回两个日期的天数差，例1998-4-21~1998-4-25 相差4天，返回4
     */
    public static int getDiffDays(Date fromDate, Date toDate) {
        return (int) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 获取两个日期的秒数差
     *
     * @param fromDate 起始日期
     * @param toDate   结束日期
     * @return 返回两个日期的差，结束日期减去起始日期
     */
    public static Long getDiff(Date fromDate, Date toDate) {
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(fromDate);

        Calendar toCal = Calendar.getInstance();
        toCal.setTime(toDate);

        Long diff = (toCal.getTime().getTime() - fromCal.getTime().getTime());
        return diff;
    }

    /**
     * 两个日期的秒数差
     *
     * @param fromDate 起始日期
     * @param toDate   结束日期
     * @return 返回两个日期的秒数差，例1998-4-21 10:00:00~1998-4-21 10:00:50 相差50秒，返回50
     */
    public static Long getDiffSeconds(Date fromDate, Date toDate) {
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(fromDate);
        fromCal.set(Calendar.MILLISECOND, 0);

        Calendar toCal = Calendar.getInstance();
        toCal.setTime(toDate);
        toCal.set(Calendar.MILLISECOND, 0);
        Long diff = (toCal.getTime().getTime() - fromCal.getTime().getTime()) / 1000;
        return diff;
    }

    /**
     * 获取一个星期中的第几天，周日算第一天
     *
     * @param date 待解析的日期
     * @return 返回一个星期中的第几天
     */
    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**
     * 获取一个星期中的第几天，周一算第一天
     *
     * @param date 待解析的日期
     * @return 返回一个星期中的第几天
     */
    public static int getChinaDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (1 == dayOfWeek) {
            dayOfWeek = 8;
        }
        return dayOfWeek - 1;
    }

    /**
     * 获取一个月中的第几天，一个月中第一天的值为1
     *
     * @param date 待解析的日期
     * @return 返回一个月中的第几天
     */
    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth;
    }

    /**
     * 获取当前时间的时间戳，精确到毫秒
     *
     * @return 返回当前时间的时间戳
     */
    public static Long getTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取某日的0时0分0秒的Date对象
     *
     * @param datetime 待解析的日期
     * @return 传入日期的0时0分0秒的Date对象
     */
    public static Date getDayStart(Date datetime) {
        if (null == datetime) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(datetime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取某日的23时59分59秒的Date对象
     *
     * @param datetime 待解析的日期
     * @return 传入日期的23时59分59秒的Date对象
     */
    public static Date getDayEnd(Date datetime) {
        if (null == datetime) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(datetime);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获取某月第一天的0时0分0秒的Date对象
     *
     * @param datetime 待解析的日期
     * @return 指定日期所在月份的第一天时间
     */
    public static Date getMonthDayStart(Date datetime) {
        if (null == datetime) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(datetime);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取某月最后一天 Create Time May 3, 2012 1:37:53 PM
     *
     * @param datetime
     * @return
     */
    public static Date getMonthDayEnd(Date datetime) {
        if (null == datetime) {
            return null;
        }
        Date date = datetime;
        date = addMonth2Date(1, date);
        date = getMonthDayStart(date);
        date = addDay2Date(-1, date);
        return date;
    }

    /**
     * 由Timestamp类型对象转换成Date类型对象
     *
     * @param timestamp Timestamp类型对象
     * @return Date型日期对象
     */
    public static Date transToDate(Timestamp timestamp) {
        if (timestamp != null) {
            return new Date(timestamp.getTime());
        }
        return null;
    }

    /**
     * 遍历指定年月，以周为循环生成一个6*7的二维数组，空闲位为Null
     *
     * @param year  指定年
     * @param month 指定月
     * @return Date数组对象
     */
    public static Date[][] makeCalendar(int year, int month) {
        Date[][] dateArray = new Date[6][7];
        // 指定年月的第一天
        Date date = DateTimeUtil.getDateYMD(year + "-" + month + "-01");
        // 次月的第一天
        Date lastDate = DateTimeUtil.addMonth2Date(1, date);
        // 第一天是周几
        int firstDayWeek = DateTimeUtil.getDayOfWeek(date);
        // 将星期日处理为一周的最后一天
        if (1 == firstDayWeek) {
            firstDayWeek = 8;
        }
        int row = 0;
        int col = firstDayWeek - 2;
        // 遍历一个月，以周为循环生成二维数组
        while (DateTimeUtil.getDiffDays(date, lastDate) > 0) {
            if (col > 6) {
                row = row + 1;
                col = 0;
            }
            dateArray[row][col] = date;
            date = DateTimeUtil.addDay2Date(1, date);
            col++;
        }

        return dateArray;
    }

    /**
     * 根据给定的开始、结束日期，以周为循环生成一个n*7列的二维数组，空闲位为Null
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return Date[][]
     */
    public static Date[][] makeCalendar(Date startDate, Date endDate) {
        int n = getDiffDays(startDate, endDate) / 7 + 1;
        Date[][] dateArray = new Date[n][7];
        // 开始日期是周几
        int firstDayWeek = DateTimeUtil.getDayOfWeek(startDate);

        // 将星期日处理为一周的最后一天
        if (1 == firstDayWeek) {
            firstDayWeek = 8;
        }
        int row = 0;
        int col = firstDayWeek - 2;
        // 遍历开始日期和结束日期之间的所有日期（包括开始日期和结束日期），以周为循环生成二维数组
        while (DateTimeUtil.getDiffDays(startDate, endDate) >= 0) {
            if (col > 6) {
                row = row + 1;
                col = 0;
            }
            dateArray[row][col] = startDate;
            startDate = DateTimeUtil.addDay2Date(1, startDate);
            col++;
        }

        return dateArray;
    }

    /**
     * 获取指定年份的休息日列表
     *
     * @param year 指定年
     * @return 指定年份的休息日列表
     */
    public static List<Date> getWeekEndList(Integer year) {
        Date date = DateTimeUtil.getDateYMD(year + "-01-01");
        // 次年的第一天
        Date lastDate = DateTimeUtil.addYear2Date(1, date);
        List<Date> weekendList = new ArrayList<Date>();
        while (DateTimeUtil.getDiffDays(date, lastDate) > 0) {
            int dayOfweek = DateTimeUtil.getChinaDayOfWeek(date);
            if (6 == dayOfweek || 7 == dayOfweek) {
                weekendList.add(date);
            }
            date = DateTimeUtil.addDay2Date(1, date);
        }
        return weekendList;
    }

    public static String getSimpleTime(String dateTime) {
        long ONE_MINUTE = 60;
        long ONE_HOUR = 3600;
        long ONE_DAY = 86400;

        Date date = getDate(dateTime, "yyyy-MM-dd HH:mm:ss");

        long time = date.getTime() / 1000;
        long now = (new Date().getTime()) / 1000;
        long ago = now - time;

        if (ago <= ONE_HOUR)
            return ago / ONE_MINUTE + "分钟前";
        else if (ago <= ONE_DAY)
            return ago / ONE_HOUR + "小时前";
        else if (ago <= ONE_DAY * 5)
            return ago / ONE_DAY + "天前";
        else
            return dateTime;
    }
}
