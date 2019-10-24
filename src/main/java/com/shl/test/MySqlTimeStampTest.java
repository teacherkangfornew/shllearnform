package com.shl.test;

import com.shl.util.CommonUtil;
import com.shl.util.DbBase;
import com.shl.util.Dbutil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlTimeStampTest extends DbBase {


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MySqlTimeStampTest mySqlTimeStampTest = new MySqlTimeStampTest();
        mySqlTimeStampTest.insert();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        System.out.println(sdf.format(mySqlTimeStampTest.getDate()));
    }

    Date getDate() throws SQLException, ClassNotFoundException {
        begin();
        String sql = "select date from timetest ";
        PreparedStatement pstmt = this.conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return (Date) rs.getObject(1);
        }
        return null;
    }

    void insert() {
        Dbutil db = new Dbutil();
        String sql = "insert into timetest values(:id,:date) ";
        Map<String, Object> varMap = new HashMap<>(8);
        varMap.put("id", CommonUtil.UUID());
        varMap.put("date", new Date());
        db.executeUpdate(sql, varMap);
    }
}
