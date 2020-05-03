package com.shl.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 访问第三方网站接口  post
 * @author xc_004
 */
public class NetBase {
    private static final String API_URL = "http://tieyi.xiaoyuanbangong.com";
    private String token;

    public NetBase() {
        this.token = getToken();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>default charset name = " + Charset.defaultCharset().name());
        NetBase nb = new NetBase();
        nb.getTeacherTb();
        System.out.println("---------------------------------------------------------------------------");
        nb.getClassTb();
    }

    public void getClassTb() throws IOException {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("s", token);
        varMap.put("class_id", "yxg20190107");
        StringBuilder result = putConnection(varMap, "classTb");
    }

    public void getTeacherTb() throws IOException {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("s", token);
        varMap.put("tel", "18818910532");
        StringBuilder result = putConnection(varMap, "teacherTb");
        Map data = JsonUtils.readValueByClass(result.toString(), Map.class);
    }


    private String getToken() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("acc", "liangcai");
        StringBuilder result = null;
        try {
            result = putConnection(varMap, "getTicket");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotBlank(result)) {
            Map response = JsonUtils.readValueByClass(result.toString(), Map.class);
            assert response != null;
            Object token = response.get("data");
            if (token != null && StringUtils.isNotBlank(token.toString())) {
                System.out.println("token = " + token);
                return token.toString();
            }
        }
        throw new RuntimeException("获取连接不合法");
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
        conn.setDoInput(true);
        conn.setDoOutput(true);
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
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line = "";
        StringBuilder result = new StringBuilder();
        while (StringUtils.isNotBlank((line = bufferedReader.readLine()))) {
            result.append(line);
        }
        System.out.println(result);
        return result;
    }
}
