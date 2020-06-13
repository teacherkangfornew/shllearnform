package com.shl.test;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 访问第三方网站接口  post
 * @author xc_004
 */
public class NetBase2 {
    private static final String API_URL = "http://localhost:8080/xcoffice/api/classcreen/stuspace";
    private String token;

    public NetBase2() {
        this.token = getToken();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>default charset name = " + Charset.defaultCharset().name());
        /*NetBase2 nb = new NetBase2();
        // nb.createData();
        HashMap<String, Object> varMap = new HashMap<>();
        varMap.put("App-Authorization", "XCOffice eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ4Y2FkbWluIiwic3ViIjoiOGFhNTc2YWI5MTQ3NDNiMTkxNDdkYTc4YzA3MTZmMWEiLCJleHAiOjE1ODI5MDIwMjYsIm5iZiI6MTU4Mjg3MzIyNiwiaWF0IjoxNTgyODczMjI2LCJqdGkiOiIxYjU1YjRhYy0xNTQ0LTRmYTgtYmQ3MC1mZTQwN2NlN2I3NGEiLCJ0dHkiOiJhY2Nlc3NUb2tlbiJ9.anwbEknVXuHXrlX7LBtRqRhSUWdA1HI5kOCsfTWXCYo");
        nb.putConnectionGet(varMap, "http://192.168.124.16:8080/xcoffice/api/clascreen/attachment/batchdownloadstuphoto");*/

        String pathname = "E:/share/aaa";
        File f = new File(pathname);
        if (f.isDirectory()) {
            System.out.println(">>>>>>>>>>>>>>>>11");
        } else {
            System.out.println(">>>>>>>>>>>>>..2");
        }
    }

    public void createData() throws IOException {
        Map<String, Object> varMap = new HashMap<>(8);
        varMap.put("stuId", "2c90abb66c9d6c35016c9da161960087");

        varMap.put("leaveTypeId", "1");


        varMap.put("leaveTypeName", "其他");


        varMap.put("takeLeaveStartTime", "2020-02-25 13:00");


        varMap.put("takeLeaveEndTime", "2020-02-25 15:00");


        varMap.put("leaveToId", 0);


        varMap.put("leaveReason", "看老师不爽");



        this.putConnection(varMap, "submitLeave");

    }



    private String getToken() {
        String token = "XCOffice eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ4Y2FkbWluIiwic3ViIjoiOGFhNTc2YWI5MTQ3NDNiMTkxNDdkYTc4YzA3MTZmMWEiLCJleHAiOjE1ODI2NDQwMzksIm5iZiI6MTU4MjYxNTIzOSwiaWF0IjoxNTgyNjE1MjM5LCJqdGkiOiJhZWIyYzVmMy1iMGI3LTRkMDMtOGI3NC04OTk2ZjI4YTI0NGMiLCJ0dHkiOiJhY2Nlc3NUb2tlbiJ9.d42exoOaE73b9mr-cpwVHbNUS0lFtEhO_LAJxv8fg8I";
        return token;
    }

    private StringBuilder putConnection(Map<String, Object> varMap, String methodName) throws IOException {
        URL url = new URL(API_URL + "/" + methodName);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        conn.setRequestProperty("App-Authorization", this.token);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 不允许使用缓存
        conn.setUseCaches(false);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : varMap.entrySet()) {
            sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue()+"", "UTF-8")).append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        System.out.println(sb);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(sb);
        out.flush();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        String line = "";
        StringBuilder result = new StringBuilder();
        while (StringUtils.isNotBlank((line = bufferedReader.readLine()))) {
            result.append(line);
        }
        System.out.println(result);
        return result;
    }

    private void putConnectionGet(Map<String, Object> varMap, String urls) throws IOException {
        URL url = new URL(urls);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Cookie", "epname=shenhl; JSESSIONID=aee89d19-3888-4637-806c-922c938a9478");
        // conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        /// conn.setRequestProperty("App-Authorization", this.token);
        for (Map.Entry<String, Object> entry : varMap.entrySet()) {
            conn.setRequestProperty(URLEncoder.encode(entry.getKey(), "UTF-8"), URLEncoder.encode(entry.getValue() + "", "UTF-8"));
        }
        conn.connect();
        InputStream in = conn.getInputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        File file = new File("E:/share");
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry entry;
        while ((entry  = zin.getNextEntry()) != null) {
            String dir = file.getPath() + File.separator + entry.getName();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dir));

            while ((len = zin.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
        }
    }
}
