package com.shl.test;

import com.shl.util.CommonUtil;
import com.shl.util.Dbutil;

import java.util.*;

public class ClassScreenModel {

    public static void main(String[] args) {
        ClassScreenModel csm = new ClassScreenModel();
        Calendar c = Calendar.getInstance();
        Map<String, Object> varMap = new HashMap<>();
        System.out.println(">>>>>>>>" + varMap.get("sss") + "");

    }

    // 构建宿舍签到学生数据
    public void createDorSignRecord() {
        String campusId = "2c90abb66c9d6c35016c9d947b2b004e";
        String sql = "SELECT " +
                " aaa.id AS stuid, " +
                " aaa.student_code AS stucode, " +
                " aaa.student_name AS stuname, " +
                " bbb.class_code AS classcode, " +
                " bbb.class_name AS classname, " +
                " bbb.id AS classid, " +
                " ccc.id AS gradeid, " +
                " ccc.grade_name AS gradename, " +
                " bbb.campus_id AS campusid, " +
                " eee. NAME AS building_no, " +
                " ddd.building_no AS dorbuildid, " +
                " ddd.dormitory_no, " +
                " ddd.floor_no, " +
                " ddd.bed_no, " +
                " aaa.img_id AS imgid, " +
                " fff.checkcode " +
                " FROM " +
                " edu_base_student aaa " +
                "LEFT JOIN edu_base_class bbb ON aaa.class_id = bbb.id " +
                "LEFT JOIN edu_base_grade ccc ON bbb.grade_id = ccc.id " +
                "LEFT JOIN edu_base_resident_student ddd ON aaa.student_code = ddd.student_code " +
                "LEFT JOIN sys_core_dictdata eee ON eee.id = ddd.building_no " +
                "LEFT JOIN xc_sys_attachment fff ON fff.id = aaa.img_id " +
                " " +
                "where ddd.building_no is not null ";
        Dbutil db = new Dbutil();
        List<Map> list = db.findListMapByNativeSql(sql, null);

         sql = "INSERT INTO edu_classcreen_stu_sign_dor " +
                "(id,  isdelete,  optlock, campus_id, class_id, domain_id, grade_id," +
                " appvoval_id, sign_status, stu_code, stu_id, term_id, dor_build_id, " +
                "dor_floor_id, dor_room_id, sign_time) " +
                " VALUES (:id,  0, 0, :campusId, :classId, 0, :gradeId," +
                " '', :signStatus, :stuCode, :stuId, '', :buildId, :floorId, :roomId, :signTime )";

        Map<String, Object> varMap = new HashMap<>(8);
        Random random = new Random(System.currentTimeMillis());
        int i = 0;
        for (Map ele : list) {
            varMap.put("id", CommonUtil.UUID());
            varMap.put("campusId", campusId);
            varMap.put("classId", ele.get("classid"));
            varMap.put("gradeId", ele.get("gradeid"));
            varMap.put("signStatus", 0);
            varMap.put("stuCode", ele.get("stucode"));
            varMap.put("stuId", ele.get("stuid"));
            varMap.put("buildId", ele.get("dorbuildid"));
            varMap.put("floorId", ele.get("floor_no"));
            varMap.put("roomId", ele.get("floor_no"));
            varMap.put("signTime", new Date());
            db.executeUpdate(sql, varMap);
            if (i == 5) {
                break;
            }
            i++;
            varMap.clear();
        }
    }

}
