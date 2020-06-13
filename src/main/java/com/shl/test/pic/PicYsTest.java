package com.shl.test.pic;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;

public class PicYsTest {
    public static void main(String[] args) throws IOException {
        String picPath = "D:/picys/";
        Thumbnails.of(picPath + "eee.bmp")
                .scale(1f).outputQuality(0.15f)
                .toFile(picPath + "e.jpg");

        System.out.println(System.currentTimeMillis());
    }
}
