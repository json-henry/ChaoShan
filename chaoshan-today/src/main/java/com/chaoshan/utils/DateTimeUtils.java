package com.chaoshan.utils;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: HYX
 * @CreateTime: 2022-06-03  16:09
 * @Description: 时间工具类
 * @Version: 1.0
 */
public class DateTimeUtils {

    /**
     * 判断传入的时间是否是今天
     *
     * @param dateTime
     * @return
     */
    public static boolean isToday(LocalDateTime dateTime) {
        LocalDate date = LocalDateTimeUtil.parseDate(dateTime.toString(), DateTimeFormatter.ISO_DATE_TIME);
        return date.isEqual(LocalDate.now());
    }

}


