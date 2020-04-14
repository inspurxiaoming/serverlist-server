package com.ruoyi.common.utils;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * desc:
 *
 * @param
 * @author chengym
 * @return
 * @date 2018/12/19 8:47
 */
public class TimeUtils {
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 字符串转date
     */
    public static Date dateFormat(String time) {
        if (!StringUtils.hasText(time)) {
            return new Date();
        }
        try {
            return threadLocal.get().parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * Date to String
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return threadLocal.get().format(date);
    }

    public static Date longToDate(Long date){
        return new Date(date);
    }
}
