package com.shl.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbBase {

	private static final String URL;
	private static final String PWD;
	private static final String USER;
	private static final String DRIVER;
	private static Properties dbconfig = new Properties();
	
	protected Connection conn;
	static {
		try {
			// dbconfig.properties
			InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("dbconfig.properties");
			dbconfig.load(in);
		} catch (IOException e) {
			System.out.println(">>>读取配置错误");
			e.printStackTrace();
		}
		URL = dbconfig.getProperty("dburl");
		USER = dbconfig.getProperty("dbuser");
		PWD = dbconfig.getProperty("dbpwd");
		DRIVER = dbconfig.getProperty("dbdriver");
	}
	
	protected void begin() throws ClassNotFoundException, SQLException{
		Class.forName(DRIVER);
		conn = DriverManager.getConnection(URL, USER, PWD);
	}
	
}
