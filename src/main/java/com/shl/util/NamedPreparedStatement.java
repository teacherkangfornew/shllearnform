package com.shl.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class NamedPreparedStatement {

	private Connection conn;
	private String query;
	@SuppressWarnings("rawtypes")
	private Map indexMap;
	private PreparedStatement pstmt;
	@SuppressWarnings("rawtypes")
	public NamedPreparedStatement(Connection conn,String query){
		this.conn = conn;
		this.query = query;
		this.indexMap = new HashMap();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String parse(){
		int len = this.query.length();
		StringBuffer parseSql = new StringBuffer(len);
		char c;
		int index = 1;
		int j;
		String name;
		List tempList = null;
		for (int i = 0; i < len; i++){
			c = this.query.charAt(i);
			if (c == '\'' || c == '\"'){
				//nothing
			}else if (c == ':' && i+1 < len && Character.isJavaIdentifierStart(this.query.charAt(i+1))){
				j = i+2;
				while ( j < len && Character.isJavaIdentifierPart(this.query.charAt(j))){
					j++;
				}
				
				name = this.query.substring(i+1, j);
				c = '?';
				i += name.length();
				
				tempList = (List)this.indexMap.get(name);
				if (tempList == null){
					tempList = new ArrayList();
					this.indexMap.put(name, tempList);
				}
				tempList.add(index);
				index++;
			}
			
			parseSql.append(c);
		}
		transIndexMap();
		System.out.println(">>>>>>>>>>>>>sql = " + parseSql);
		return parseSql.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public void setVarMap(Map varMap) throws SQLException{
		Map paraMap = null;
		if (varMap != null){
			paraMap = new HashMap();
			transMap(varMap,paraMap,"100");
		}
		
		Map otherMap = null;
		if (paraMap != null){
			otherMap = new HashMap();
			transMap(paraMap,otherMap,"-100");
		}
		
		if (otherMap != null){
			transMap(otherMap,paraMap,"100");
		}
		
		String parseSql = parse();
		
		this.pstmt = conn.prepareStatement(parseSql);
		
		if (paraMap != null) {
			Iterator it = paraMap.entrySet().iterator();
			Map.Entry entry = null;
			String key = null;
			Object value = null;
			while (it.hasNext()) {
				entry = (Map.Entry) it.next();
				key = entry.getKey() + "";
				value = entry.getValue();
				if (value instanceof Date) {
					setObjects(key,value,"-100");
				} else {
					setObjects(key,value,"100");
				}
			}
		}
	}
	

	private void setObjects(String key,Object value,String flag) throws SQLException{
		int[] indexes = this.getIndexesByName(key);
		if (indexes == null) {
			return;
		}
		if ("100".equals(flag)){
			for (int i = 0; i < indexes.length; i++){
				this.pstmt.setObject(indexes[i], value);
			}
		}else {
			for (int i = 0; i < indexes.length; i++){
				this.pstmt.setTimestamp(indexes[i], new java.sql.Timestamp(((Date)value).getTime()));
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void transIndexMap(){
		Iterator it = this.indexMap.entrySet().iterator();
		Iterator it2 = null;
		Map.Entry entry = null;
		int[] indexes = null;
		List tempList = null;
		int i = 0;
		while (it.hasNext()){
			entry = (Map.Entry)it.next();
			tempList = (List)entry.getValue();
			if (tempList!=null){
				i = 0;
				indexes = new int[tempList.size()];
				for ( it2 = tempList.iterator(); it2.hasNext(); ){
					indexes[i++] = Integer.valueOf(it2.next() + "");
				}
				entry.setValue(indexes);
			}
		}
	}
	
	private int[] getIndexesByName(String name){
		return (int[])this.indexMap.get(name);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void transMap(Map src,Map dicMap,String flag){
		Iterator it = src.entrySet().iterator();
		Map.Entry entry = null;
		String key = null;
		Object value = null;
		while (it.hasNext()){
			entry = (Map.Entry)it.next();
			key = entry.getKey() + "";
			if ("100".equals(flag)){
				if (this.query!=null && this.query.indexOf(":" + key) != -1){
					dicMap.put(key, src.get(key));
				}
			}else {
				value = entry.getValue();
				if (value instanceof Object[]){
					this.query = buildArray(this.query, key,(Object[])value, dicMap);
				}else if (value instanceof Collection){
					this.query = buildArray(this.query, key,((Collection)value).toArray(), dicMap);
				}
			}
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String buildArray(String sql,String key,Object[] obj,Map varMap){
		StringBuffer result = new StringBuffer();
		String inKey = "";
		for (int i = 0; i < obj.length; i++){
			inKey = key+"_IN_" + i;
			result.append(":").append(inKey).append(",");
			varMap.put(inKey, obj[i]);
		}
		
		if (result.length() > 0){
			result.deleteCharAt(result.length()-1);
		}
		sql = sql.replaceAll(":"+key, result.toString());
		return sql;
	}
	
	public ResultSet executeQuery() throws SQLException{
		return this.pstmt.executeQuery();
	}
	
	public int executeUpdate() throws SQLException{
		return this.pstmt.executeUpdate();
	}
}
