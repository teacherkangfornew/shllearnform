package com.shl.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Dbutil extends DbBase {

	@SuppressWarnings("rawtypes")
	public List<Object[]> getDataListBySQL(String sql,Map varMap){
		List<Object[]> result = new LinkedList<>();
		try {
			begin();
			NamedPreparedStatement nps = new NamedPreparedStatement(conn,sql);
			nps.setVarMap(varMap);
			ResultSet rs = nps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNum = rsmd.getColumnCount();
			String needTime = getTimeTypeColumnIndex(columnNum,rsmd);
			createResultList(result, rs, columnNum, needTime);
		}catch( SQLException sqlException){
			System.out.println(">>>执行SQL错误");
			sqlException.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println(">>>加载数据库出错");
			e.printStackTrace();
		}
		return result;
	}

	public List<Map> findListMapByNativeSql(String sql,Map varMap){
		List<Map> result = new LinkedList<>();
		try {
			begin();
			NamedPreparedStatement nps = new NamedPreparedStatement(conn,sql);
			nps.setVarMap(varMap);
			ResultSet rs = nps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNum = rsmd.getColumnCount();
			String needTime = getTimeTypeColumnIndex(columnNum,rsmd);
			resulListMap(result, rs, columnNum, needTime);
		}catch( SQLException sqlException){
			System.out.println(">>>执行SQL错误");
			sqlException.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println(">>>加载数据库出错");
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public int executeUpdate(String sql,Map varMap){
		int affectNum = -1;
		try {
			begin();
			NamedPreparedStatement nps = new NamedPreparedStatement(conn,sql);
			nps.setVarMap(varMap);
			affectNum = nps.executeUpdate();
		} catch (ClassNotFoundException e) {
			System.out.println(">>>执行SQL错误");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println(">>>加载数据库出错");
			e.printStackTrace();
		}
		return affectNum;
	}
	
	@SuppressWarnings("rawtypes")
	public Object[] getFirstDataBySQL(String sql,Map varMap){
		List<Object[]> result = getDataListBySQL(sql, varMap);
		if (result != null && result.size() > 0){
			return result.get(0);
		}
		return null;
	}

	@SuppressWarnings("Duplicates")
	public List<Object[]> getDataListBySQL(Connection conn, String sql, Map varMap){
		List<Object[]> result = new LinkedList<>();
		try {
			NamedPreparedStatement nps = new NamedPreparedStatement(conn,sql);
			nps.setVarMap(varMap);
			ResultSet rs = nps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNum = rsmd.getColumnCount();
			String needTime = getTimeTypeColumnIndex(columnNum,rsmd);
			createResultList(result, rs, columnNum, needTime);
		}catch( SQLException sqlException){
			System.out.println(">>>执行SQL错误");
			sqlException.printStackTrace();
		}
		return result;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createResultList(List result,ResultSet rs,int columnNum,String needTime ) throws SQLException{
		Object[] obj;
		while (rs.next()){
			obj = new Object[columnNum];
			for (int i = 1; i <= columnNum; i++){
				if (needTime.contains("," + i + ",")){
					obj[i-1] = rs.getTimestamp(i);
				}else {
					obj[i-1] = rs.getObject(i);
				}
			}
			result.add(obj);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void resulListMap(List result,ResultSet rs,int columnNum,String needTime ) throws SQLException{
		Map ele;
		String lable;
		ResultSetMetaData rsmd = rs.getMetaData();
		while (rs.next()){
			ele = new HashMap();
			for (int i = 1; i <= columnNum; i++){
				lable = rsmd.getColumnLabel(i);
				if (needTime.contains("," + i + ",")){
					ele.put(lable, rs.getTimestamp(i));
				}else {
					ele.put(lable, rs.getObject(i));
				}
			}
			result.add(ele);
		}
	}
	
	private String getTimeTypeColumnIndex(int columnNum,ResultSetMetaData rsmd) throws SQLException{
		String columnType;
		StringBuilder result = new StringBuilder();
		for (int i = 1; i <= columnNum; i++){
			columnType = rsmd.getColumnTypeName(i);
			if (columnType != null && "DATE".equals(columnType.toUpperCase())){
				result.append(",").append(i).append(",");
			}
		}
		return result.toString();
	}
}
