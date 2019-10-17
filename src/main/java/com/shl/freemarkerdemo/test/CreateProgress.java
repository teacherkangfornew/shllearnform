package com.shl.freemarkerdemo.test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CreateProgress {
    /**
     * 创建一个BufferedImage图片缓冲区对象并指定它宽高和类型 这样相当于创建一个画板，然后可以在上面画画
     */
    BufferedImage bi = new BufferedImage(200, 200, BufferedImage.TYPE_INT_BGR);

    /**
     * 要生成图片的类型,可以是JPG GIF JPEG PNG等...
     */
    final String picType = "png";

    /**
     * 成生成图片的保存路径和图片名称
     */
    final File file = new File(picType + ".jpg");

    /**
     * 通过指定参数写一个图片
     *
     * @param bi
     * @param picType
     * @param file
     * @return boolean 如果失败返回一个布尔值
     */
    private   void writeImage(int width, int height) {
        // 拿到画笔
        Graphics g = bi.getGraphics();
        // 画一个图形系统默认是白色
        g.fillRect(0, 0, width, height);
        // 设置画笔颜色
        g.setColor(Color.RED);

        // 设置画笔画出的字体风格
       // g.setFont(new Font("隶书", Font.ITALIC, 30));
        // 写一个字符串
        // g.drawString("我是IO流图片", 10, 100);
        // 释放画笔
        g.dispose();

    }

    public  void draw(String path, String name) {
        writeImage( 200, 30);
        writeImage(1000, 30);
        // 将画好的图片通过流形式写到硬盘上
        boolean val = false;
        try {
            val = ImageIO.write(bi, picType, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CreateProgress c = new CreateProgress();
        c.draw("", "");

    }

}
