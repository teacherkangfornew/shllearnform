package com.shl.test.iotest;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class FileTest {



    public static void main(String[] args) {

        bufferInputStreamTest();
    }

    static void bufferInputStreamTest() {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream("D:/dbconfig.properties"));
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = bis.read(buffer)) != -1) {
                System.out.println(new String(buffer, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void FileTest() {

    }
}
