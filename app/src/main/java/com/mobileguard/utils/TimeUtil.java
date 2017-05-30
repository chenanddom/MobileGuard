package com.mobileguard.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 获取日期的功能类
 * Created by chendom on 2017/4/17 0017.
 */

public class TimeUtil {
    public static String getCuurentTime(){
        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(date);
        return currentTime;
    }
}
