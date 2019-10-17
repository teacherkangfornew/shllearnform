package com.shl.util;

import java.sql.*;
import java.util.*;

public final class CommonUtil {
    public static String UUID() {
        return UID().replaceAll("-", "");
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
