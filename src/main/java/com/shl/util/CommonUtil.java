package com.shl.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonUtil {
    public static String UUID() {
        return UID().replaceAll("-", "");
    }

    public static final Pattern DIGREG = Pattern.compile("(\\+?([0-9]+|[0-9]+\\.[0-9]+)|-?([0-9]+|[0-9]+\\.[0-9]+))");

    public static boolean isNumber(String num) {
        if (StringUtils.isBlank(num)) {
            return false;
        }
        Matcher m = DIGREG.matcher(num);
        return m.matches();

    }

    public static boolean isFloat(String num) {
        return isNumber(num);
    }

    public static boolean isInteger(String num) {
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    public static boolean isFloat(String num, Integer floatnum) {
        String regx;
        if (floatnum == null) {
            return isNumber(num);
        } else if (floatnum == 0) {
            return isInteger(num);
        } else if (floatnum == 1) {
            regx = "(\\+?([0-9]+|[0-9]+\\.[0-9])|-?([0-9]+|[0-9]+\\.[0-9]))";
        } else {
            regx = "(\\+?([0-9]+|[0-9]+\\.[0-9]{1," + floatnum + "})|-?([0-9]+|[0-9]+\\.[0-9]{1," + floatnum + "}))";
        }
        return Pattern.compile(regx).matcher(num).matches();
    }

    public static class dBurlManager {
        public static final String GTYZ = "jdbc:mysql://192.168.20.63:3306/gtyz?characterEncoding=UTF-8";
        public static final String LOCAL = "jdbc:mysql://localhost:3306/xiaocoffice?characterEncoding=UTF-8";
    }

    public static Connection getXcConnection(String dBurl) throws ClassNotFoundException, SQLException {
        if (conn == null) {
            synchronized (CommonUtil.class) {
                if (conn == null) {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection(dBurl, "root", "12345");
                }
            }
        }
        return conn;
    }

    private static Connection conn;

    private static String UID() {
        return UUID.randomUUID().toString();
    }

}
