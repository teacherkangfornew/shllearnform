package com.shl.test;

import com.shl.util.CommonUtil;
import com.shl.util.Dbutil;
import com.shl.vo.VO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo implements Callable<VO> {
    // 班级 id
    private String classId;
    // 分班时间
    private Date fbDate;

    public FutureTaskDemo(String classId, Date fbDate) {
        this.classId = classId;
        this.fbDate = fbDate;
    }

    @Override
    public VO call() throws Exception {
        // 相同得计算过程

        List<Object[]> list = this.getData(this.classId);
        if (list != null) {
            System.out.println(">>>>>>>>" + this.classId + ".........数据长度 = " + list.size());
        }
        VO vo = new VO();
        vo.setClassId(this.classId);
        return vo;
    }

    private List<Object[]> getData(String classId) throws SQLException, ClassNotFoundException {
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
                " WHERE  aaa.class_id =:classId " +
                " and aaa.campus_id = '2c90abb66c9d6c35016c9d947b2b004e'" +
                " AND bbb.id = aaa.class_id" +
                " AND aaa.term_status<> 1" +
                " AND ccc.id = bbb.grade_id" +
                " ORDER BY " +
                " cycledate ASC ";
        Dbutil db = new Dbutil();
        Map<String, Object> varMap = new HashMap<>(8);
        varMap.put("classId", classId);
        return db.getDataListBySQL(sql, varMap);

    }

}



