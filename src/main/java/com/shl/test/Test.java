package com.shl.test;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Test {
    private static Properties dbconfig = new Properties();
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        String path = ClassLoader.getSystemClassLoader().getResource(".").getPath();
        System.out.println(path);

    }
    private static void setHMSM(int h, int m, int s, Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, h);
        c.set(Calendar.MINUTE, m);
        c.set(Calendar.SECOND, s);
        c.set(Calendar.MILLISECOND, 0);
    }

    private static void mergePdfFiles(List<String> files, String newfile) {
        long start = System.currentTimeMillis();
        Document document = null;
        List<File> tempFileList = new LinkedList<>();
        File eleFile;
        PdfImportedPage page;
        PdfReader reader;
        PdfReader pdfReader = null;
        PdfCopy copy = null;
        try {
            pdfReader = new PdfReader(files.get(0));
            document = new Document(pdfReader.getPageSize(1));
            copy = new PdfCopy(document, new FileOutputStream(newfile));
            document.open();
            for (String file : files) {
                eleFile = new File(file);
                reader = new PdfReader(file);
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
                tempFileList.add(eleFile);
            }

        } catch (Exception e) {
            System.out.println(">>>>>>>>>>>>>合并pdf发生错误");
            e.printStackTrace();
        } finally {
            if (copy != null) {
                copy.close();
            }
            if (pdfReader != null) {
                pdfReader.close();
            }
            if (document != null) {
                document.close();
            }
            // 删除file
            for (File file : tempFileList) {
                file.delete();
            }
        }
        System.out.println(">>>>>>>>>>>>>>合并pdf耗时 : " + (System.currentTimeMillis() - start) / 1000.0 + "秒");
    }

    public static void imagesToPdf(String fileName, String imagesPath) {
        try {
            fileName ="D:/aaa.pdf";
            File file = new File(fileName);
            // 第一步：创建一个document对象。
            Document document = new Document();
            document.setMargins(0, 0, 0, 0);
            // 第二步：
            // 创建一个PdfWriter实例，
            PdfWriter.getInstance(document, new FileOutputStream(file));
            // 第三步：打开文档。
            document.open();
            // 第四步：在文档中增加图片。
            File files = new File(imagesPath);
            String[] images = files.list();
            int len = images.length;

            for (int i = 0; i < len; i++)
            {
                if (images[i].toLowerCase().endsWith(".bmp")
                        || images[i].toLowerCase().endsWith(".jpg")
                        || images[i].toLowerCase().endsWith(".jpeg")
                        || images[i].toLowerCase().endsWith(".gif")
                        || images[i].toLowerCase().endsWith(".png")) {
                    String temp = imagesPath + "\\" + images[i];
                    try {
                        Image img = Image.getInstance(temp);
                        img.setAlignment(Image.ALIGN_CENTER);
                        // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
                        document.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                        document.newPage();
                        document.add(img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            // 第五步：关闭文档。
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
