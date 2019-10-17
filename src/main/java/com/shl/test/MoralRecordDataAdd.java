package com.shl.test;

import com.shl.util.CommonUtil;
import com.shl.util.DbBase;
import com.shl.util.Dbutil;
import com.shl.vo.VO;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
public class MoralRecordDataAdd  {

    private static final int MAX = 200000;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        MoralRecordDataAdd moralRecordDataAdd = new MoralRecordDataAdd();
        moralRecordDataAdd.excute();
       /* List<Object[]> list = moralRecordDataAdd.search();
        Map<String, VO> map = null;
        long start = System.currentTimeMillis();
        try {
            map = moralRecordDataAdd.threadSearch();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(">>>>>>>>>>并发耗时 -----------" + (end - start) / 1000.0 + "秒");*/
    }

    Map<String, VO> threadSearch() throws ExecutionException, InterruptedException {
        String sql = "select  class_id  from mark_record where campus_id = '2c90abb66c9d6c35016c9d947b2b004e' GROUP BY class_id";
        List<Object[]> list = new Dbutil().getDataListBySQL(sql, null);
        List<FutureTask<VO>> taskList = new LinkedList<>();
        for (Object[] obj : list) {
            FutureTask<VO> task = new FutureTask<VO>(new FutureTaskDemo(obj[0].toString(), null));
            taskList.add(task);
            new Thread(task).start();
        }
        Map<String, VO> map = new HashMap<>(8);
        for (FutureTask<VO> t : taskList) {
            VO v = t.get();
            map.put(v.getClassId(), v);
        }

        return map;
    }

    List<Object[]> search() throws SQLException, ClassNotFoundException {
        long start = System.currentTimeMillis();
        Dbutil db = new Dbutil();
        String sql = "SELECT" +
                " aaa.class_id," +
                " bbb.class_name," +
                " aaa.quota_id," +
                " aaa.quota_code," +
                " aaa.mark_details_id," +
                " aaa.score," +
                " aaa.mark_type," +
                " aaa.id," +
                " ccc.grade_name," +
                " aaa.cycledate," +
                " aaa.islatest," +
                " aaa.week_number," +
                " aaa.stu_number" +
                " FROM " +
                " mark_record aaa," +
                " edu_base_class bbb," +
                " edu_base_grade ccc" +
                " WHERE" +
                " aaa.campus_id = '2c90abb66c9d6c35016c9d947b2b004e'" +
                " AND bbb.id = aaa.class_id" +
                " AND aaa.term_status<> 1" +
                " AND ccc.id = bbb.grade_id" +
                " ORDER BY " +
                " cycledate ASC ";
        List<Object[]> result = db.getDataListBySQL(sql, null);
        long end = System.currentTimeMillis();
        System.out.println(">>>>>>>>>>顺序耗时 -----------" + (end - start) / 1000.0 + "秒");
        return result;
    }

    void excute() throws SQLException, ClassNotFoundException {
        Connection conn = CommonUtil.getXcConnection(CommonUtil.dBurlManager.GTYZ);
        queryMoralRecord(conn);
    }

    void queryMoralRecord(Connection conn) throws SQLException {
        String sql = "select * from mark_record";
        PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("insert into mark_record values (");
        for (int i = 1; i <= columnCount; i++) {
            insertSql.append("?").append(",");
        }
        if (insertSql.toString().endsWith(",")) {
            insertSql.deleteCharAt(insertSql.length() - 1);
        }
        insertSql.append(")");
        System.out.println(insertSql.toString());
        int f = 0;
        conn.setAutoCommit(false);
        pstmt = conn.prepareStatement(insertSql.toString());
        while (rs.next()) {
            pstmt.setObject(1, CommonUtil.UUID());
            for (int i = 2; i <= columnCount; i++) {
                pstmt.setObject(i, rs.getObject(i));
            }
            f++;
            if (f < MAX && rs.isLast()) {
                System.out.println(">>>>>>>>>>>>>>>1111111");
                rs.beforeFirst();
            }
            pstmt.executeUpdate();
        }
        conn.commit();
    }
}
