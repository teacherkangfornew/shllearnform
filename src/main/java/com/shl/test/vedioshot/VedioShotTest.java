package com.shl.test.vedioshot;

import com.shl.util.CommonUtil;
import com.shl.util.Dbutil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class VedioShotTest {
    static String FFEMG_EXE_PATH = "D:/vedioshot/ffmpeg/bin/";
    public static void main(String[] args) throws IOException {

        List<String> list = new ArrayList<>(1);
        list.set(0, "11");
        System.out.println(list);
    }

    private static void courseSearch() throws FileNotFoundException {
        Dbutil dbutil = new Dbutil();

        String sql = "select student_codes from edu_timetable_class_courses ";
        List<Object[]> list = dbutil.getDataListBySQL(sql, new HashMap());
        Map<String, String> conMap = new HashMap<>(256);
        List<String> stuCodes = new LinkedList<>();
        for (Object[] obj : list) {
            String stuCode = obj[0].toString();
            String[] stuCodeArr = stuCode.split(",");
            for (String s : stuCodeArr) {
                if (!conMap.containsKey(s)) {
                    conMap.put(s, "");
                    stuCodes.add(s);
                }
            }
        }
        System.out.println(">>>>>>>>" + conMap.size());
        sql = "select cp.campus_name, sp.study_phase_name, gd.grade_name,cls.class_name,stu.student_name,stu.student_code from edu_base_student stu\n" +
                "\n" +
                "LEFT JOIN edu_base_class cls on stu.class_id = cls.id\n" +
                "\n" +
                "LEFT JOIN edu_base_grade gd on gd.id = cls.grade_id\n" +
                "\n" +
                "LEFT JOIN edu_base_studyphase sp on sp.id = gd.study_phase_id\n" +
                "\n" +
                "LEFT JOIN edu_base_campus cp on cp.id = sp.campus_id\n" +
                "\n" +
                "where stu.student_code not in (:stuCodeList)";
        Map<String, Object> varMap = new HashMap<>(1);
        varMap.put("stuCodeList", stuCodes);
        List<Object[]> stuList = dbutil.getDataListBySQL(sql, varMap);
        System.out.println(">>>>>>>>>>>" + stuList.size());
        PrintWriter writer = new PrintWriter(new File("D:/stulist.txt"));
        for (Object[] o : stuList) {
            writer.println(o[0].toString() + "  " + o[1].toString() + " " + o[2].toString() + " " + o[3].toString() + " " + o[4].toString() + " " + o[5].toString());
        }
        writer.close();
    }

    private static void propertiesTest() throws IOException {
        Properties p = new Properties();
        System.out.println(p.getProperty("hello"));

        File f = new File("D:/test.properties");
        if (!f.exists()) {
            //noinspection ResultOfMethodCallIgnored
            f.createNewFile();
        }
        FileReader reader = new FileReader(f);
        try {
            p.load(reader);
            p.setProperty("dbdriver22", "111");
            p.setProperty("lastUpdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            PrintWriter writer = new PrintWriter(new FileOutputStream("D:/test.properties"));
            p.store(writer, "haah");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void firstShot() {
        StringBuilder cmd = new StringBuilder();
        String picName = CommonUtil.UUID() + ".png";
        String vedioPath = "D:/vedioshot/星爷2.mp4";
        cmd.append(FFEMG_EXE_PATH).append("ffmpeg.exe -i ").append(vedioPath).append(" -ss 13 -f image2 ");
        cmd.append("D:/vedioshot/").append(picName);

        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(cmd.toString());
            proc.waitFor();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (proc != null && proc.isAlive()) {
                System.out.println(">>>>>>>>>>>>结束process 线程");
                proc.destroy();
            }
            System.out.println(">>>>>>>生成pdf结束");
        }
    }
}

class  B extends VedioShotTest {

}
