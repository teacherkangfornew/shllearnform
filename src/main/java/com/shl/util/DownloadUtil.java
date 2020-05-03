package com.shl.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtil {

    private String path;

    private String targetFile;

    private DownloadThread[] dThread;

    private int threadNum;

    private int fileSize;

    public DownloadUtil(String path, String targetFile, int threadNum) {
        this.path = path;
        this.targetFile = targetFile;
        this.threadNum = threadNum;
        dThread = new DownloadThread[this.threadNum];
    }

    private String authorCode;

    public void setAuthorization(String authorCode) {
        this.authorCode = authorCode;
    }

    public void download() throws IOException {
        URL url = new URL(this.path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        conn.setRequestProperty("Connection", "keep-alive");
        if (StringUtils.isNotBlank(this.authorCode)) {
            conn.setRequestProperty("Authorization", this.authorCode);
        }
        // 得到文件大小
        fileSize = conn.getContentLength();
        conn.disconnect();
        int currentPartSize = fileSize / threadNum + 1;
        RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
        file.setLength(fileSize);
        file.close();

        for (int i = 0; i < threadNum; i++) {
            int startPost = i * currentPartSize;
            RandomAccessFile r = new RandomAccessFile(targetFile, "rw");
            r.seek(startPost);
            dThread[i] = new DownloadThread(startPost, currentPartSize, r);
            dThread[i].start();
        }
    }

    public double getCompleteRate() {
        int sumSize  = 0;
        for (DownloadThread d : dThread) {
            sumSize += d.length;
        }
        return new BigDecimal((sumSize * 1.0) / fileSize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    private class DownloadThread extends Thread {

        @Override
        public void run() {
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "*/*");
                conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
                conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
                conn.setRequestProperty("Connection", "keep-alive");
                if (StringUtils.isNotBlank(DownloadUtil.this.authorCode)) {
                    conn.setRequestProperty("Authorization", DownloadUtil.this.authorCode);
                }
                InputStream in = conn.getInputStream();
                in.skip(this.startPost);
                byte[] buffer = new byte[1024];
                int hasRead = 0;
                while ((length < currentPartSize) && ((hasRead = in.read(buffer)) != -1)) {
                    currentPart.write(buffer, 0, hasRead);
                    length += hasRead;
                }
                currentPart.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public DownloadThread(int startPost, int currentPartSize, RandomAccessFile currentPart) {
            this.startPost = startPost;
            this.currentPartSize = currentPartSize;
            this.currentPart = currentPart;
        }

        // 当前线程的下载位置
        private int startPost;
        // 当前线程负责下载的大小
        private int currentPartSize;
        // 当前线程的下载块
        private RandomAccessFile currentPart;
        // 当前线程已经下载的字节数
        public int length;

    }
}
