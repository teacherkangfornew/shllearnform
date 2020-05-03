package com.shl.freemarkerdemo.test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shl.util.JsonUtils;
import com.sun.org.apache.bcel.internal.generic.DALOAD;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test {

    public static void main(String[] args) throws IOException, ParseException {
        BigDecimal b = new BigDecimal(210 * 1.0 / 5);
        System.out.println(">>>>>>>>" + b.doubleValue());
        b = b.add(new BigDecimal(50 * 1.0 / 7));
        System.out.println(">>>>>>>>." + b.doubleValue());
        BigDecimal b2 = new BigDecimal(25.0 / 31).multiply(new BigDecimal(10));
        b2 = b2.add(new BigDecimal(10.0 / 31).multiply(new BigDecimal(10)));
        b = b.subtract(b2).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(">>>" + b.doubleValue());

    }

    public static Date getLastDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 如果是周日直接返回
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return date;
        }
        System.out.println(c.get(Calendar.DAY_OF_WEEK));
        c.add(Calendar.DATE, 7 - c.get(Calendar.DAY_OF_WEEK) );
        return c.getTime();
    }

    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        c.add(Calendar.DATE, c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK) + 1);
        return c.getTime();
    }

    public static int judgeWeekNum(Date recordDate, Date lastRecordDate) {
        Calendar recordCalendar = createFirstWeekCalendar(recordDate);
        Calendar lastRecordCalendar = createFirstWeekCalendar(lastRecordDate);
        if (lastRecordCalendar.compareTo(recordCalendar) == 0) {
            // 在同一周
            return 0;
        }
        return 1;
    }

    private static Calendar createFirstWeekCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, 1);
        // 强制将 时分秒 置为 0
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c;
    }
}

class Person implements Serializable {

    private static final long serialVersionUID = 1032143110760020278L;

    private String name;

    private String age;

    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
