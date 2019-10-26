package com.shl.test;

import com.graphbuilder.math.VarMap;
import com.shl.util.CommonUtil;
import com.shl.util.DbBase;
import com.shl.util.Dbutil;
import com.shl.vo.VO;

import java.math.BigDecimal;
import java.sql.*;
import java.text.Bidi;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
public class MoralRecordDataAdd  extends DbBase{

    private static final int MAX = 200000;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        MoralRecordDataAdd moralRecordDataAdd = new MoralRecordDataAdd();
        // moralRecordDataAdd.excute();
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

        // moralRecordDataAdd.fullAddColumn();
        moralRecordDataAdd.staticall("8aa376486cb1f681016cb62b3ebe006e", "8aa376486d0b9c14016d1de5b2380203");
    }

    void staticall(String campusId, String quotaId) {
        // 将campusId 校区的所有学生信息配齐
        long allStart = System.currentTimeMillis();
        StringBuilder sql = new StringBuilder(1000);
        sql.append("SELECT " +
                " student.id, " +
                " studyphase.campus_id, " +
                " student.student_code, " +
                " student.class_id, " +
                " student.grade_id, " +
                " CONCAT( " +
                " grade.grade_name, " +
                " class.class_name " +
                " ) AS class_name, " +
                " ( " +
                " CASE " +
                " WHEN student.sex = 0 THEN " +
                " '男' " +
                " ELSE " +
                " '女' " +
                " END " +
                " ) AS sex, " +
                " student.student_name, " +
                " campus.campus_name " +
                "FROM " +
                " edu_base_student student " +
                "LEFT JOIN edu_base_class class ON student.class_id = class.id " +
                "LEFT JOIN edu_base_grade grade ON student.grade_id = grade.id " +
                "LEFT JOIN edu_base_studyphase studyphase ON student.study_phase_id = studyphase.id " +
                "LEFT JOIN edu_base_campus campus ON studyphase.campus_id = campus.id " +
                "WHERE " +
                " student.status = 0 and campus.id=:campusId " +
                "ORDER BY " +
                " studyphase.sort_num, " +
                " class.class_name, " +
                " student.student_name");

        Dbutil db = new Dbutil();
        Map<String, Object> varMap = new HashMap<>(8);
        varMap.put("campusId", campusId);
        List<Object[]> studentInfo = db.getDataListBySQL(sql.toString(), varMap);
        System.out.println(">>>>>>>>>>>>>>收集学生信息 耗时 ：" + (System.currentTimeMillis() - allStart) / 1000.0 + " 秒");
        // 所有学生id串，后面组合使用
        StringBuilder stuIds = new StringBuilder(",");
        // 每个学生id的下标是多少，方便快速定位
        Map<Integer, Integer> indexMap = new HashMap<>(415);
        int index = 0;
        List<String> stuIdsList = new LinkedList<>();
        for (Object[] o : studentInfo) {
            String str = o[0].toString();
            stuIdsList.add(str);
            indexMap.put(stuIds.length() - 1, index++);
            stuIds.append(str).append(",");
        }
        sql.setLength(0);
        // mark_record 表 查询 【主要耗时部分】
        long moralStart = System.currentTimeMillis();
        sql.append(" SELECT stu_number, " +
                " sum(mark.score) AS sum, " +
                " SUM( " +
                "  CASE " +
                "  WHEN mark.quota_id =:quotaId THEN " +
                "   mark.score " +
                "  ELSE " +
                "   0 " +
                "  END " +
                " ) AS kaoqin_xc " +
                "FROM " +
                " mark_record AS mark " +
                "LEFT JOIN moral_quota quota ON mark.quota_id = quota.id " +
                "WHERE " +
                " mark.mark_type = 1 " +
                "AND mark.campus_id =:campusId and stu_number in (:stuIds) " +
                "GROUP BY " +
                " stu_number ");
        varMap.clear();
        varMap.put("campusId", campusId);
        varMap.put("quotaId", quotaId);
        varMap.put("stuIds", stuIdsList);
        List<Object[]> scoreInfo = db.getDataListBySQL(sql.toString(), varMap);
        System.out.println(">>>>>>>>>>>>>>查询mark_record 耗时 " + (System.currentTimeMillis() - moralStart) / 1000.0 + " 秒");
        moralStart = System.currentTimeMillis();
        // 结果集
        List<VO> result = new LinkedList<>();
        // 组装数据
        for (Object[] o : scoreInfo) {
            String studentId = o[0].toString();
            index = stuIds.indexOf(("," + studentId + ","));
            if (index != -1) {
                Object[] stuObjInfo = studentInfo.get(indexMap.get(index));
                VO vo = new VO();
                vo.setCampusName(stuObjInfo[8].toString());
                vo.setClassName(stuObjInfo[5].toString());
                vo.setSex(stuObjInfo[6].toString());
                vo.setStuName(stuObjInfo[7].toString());
                vo.setScore(new BigDecimal(o[1].toString()));
                // 。。。。省略
                result.add(vo);
            }
        }

        // 排序
        result = result.stream().sorted(new Comparator<VO>() {
            @Override
            public int compare(VO v1, VO v2) {
                return  v2.getScore().subtract(v1.getScore()).signum();
            }
        }).collect(Collectors.toList());
        long allEnd = System.currentTimeMillis();
        System.out.println(">>>>>>>>>>>>组装 排序 耗时：" + (System.currentTimeMillis() - moralStart) / 1000.0 + " 秒");
        System.out.println(">>>>>>>>>>>>>总耗时 " + (allEnd - allStart) / 1000.0 + " 秒");
        for (VO vo : result) {
            System.out.println("姓名：" + vo.getStuName() + " 性别：" + vo.getSex() + " 班级:" + vo.getClassName() + " 得分:" + vo.getScore().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }

    @SuppressWarnings("JpaQueryApiInspection")
    void  fullAddColumn() throws SQLException, ClassNotFoundException {
        Dbutil db = new Dbutil();
        String sql = "select student.id, student.student_code, student.student_name " +
                ",student.sex, class.class_name,campus.campus_name " +
                " from  " +
                "edu_base_student student " +
                "LEFT JOIN edu_base_class class ON student.class_id = class.id " +
                "LEFT JOIN edu_base_studyphase studyphase ON student.study_phase_id = studyphase.id " +
                "LEFT JOIN edu_base_campus campus ON studyphase.campus_id = campus.id ";
        List<Object[]> stuInfoList = db.getDataListBySQL(sql, null);
        Map<String, VO> info = new HashMap<>(500);
        String stuId;
        for (Object[] o : stuInfoList) {
            VO vo = new VO();
            stuId = o[0].toString();
            if (!info.containsKey(stuId)) {
                vo.setStuCode(o[1].toString());
                vo.setStuName(o[2].toString());
                vo.setSex(o[3].toString());
                vo.setClassName(o[4].toString());
                vo.setCampusName(o[5].toString());
                info.put(stuId, vo);
            }
        }

        // 填充 mark_record 数据
        sql = "update mark_record set stu_name=?, class_name=?,campus_name=?,stu_sex=?,stu_code=? where stu_number=? ";
        begin();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        super.conn.setAutoCommit(false);
        for (String key : info.keySet()) {
            VO vo = info.get(key);
            int i = 1;
            pstmt.setObject(i++, vo.getStuName());
            pstmt.setObject(i++, vo.getClassName());
            pstmt.setObject(i++, vo.getCampusName());
            pstmt.setObject(i++, vo.getSex());
            pstmt.setObject(i++, vo.getStuCode());
            pstmt.setObject(i, key);
            pstmt.executeUpdate();
        }

        // 人员同步
        sql = "select id, user_name from sys_core_user where id in (select created_by from mark_record GROUP BY created_by)";
        List<Object[]> userList = db.getDataListBySQL(sql, null);
        sql = "update mark_record set created_by_name=? where created_by = ? ";
        pstmt = conn.prepareStatement(sql);
        for (Object[] o : userList) {
            pstmt.setObject(1, o[1].toString());
            pstmt.setObject(2, o[0].toString());
            pstmt.executeUpdate();
        }
        conn.commit();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>full data end ");
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
