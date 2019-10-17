package com.shl.freemarkerdemo.poitl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.util.BytePictureUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QuickStart {

    public static void main(String[] args) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "Poi-tl 模板引擎");
        map.put("pic",new PictureRenderData(100, 100, ".png", BytePictureUtils.getUrlBufferedImage("http://192.168.20.63:8185/xcoffice/static/images/pre-logo.png")) );
        RowRenderData header = RowRenderData.build(new TextRenderData("FDFFDF", "姓名"), new TextRenderData("FDFFDF", "学历"));

        RowRenderData row0 = RowRenderData.build("张三", "研究生");
        RowRenderData row1 = RowRenderData.build("李四", "博士");
        RowRenderData row2 = RowRenderData.build("王五", "博士后");

        map.put("table", new MiniTableRenderData(header, Arrays.asList(row0, row1, row2)));
        XWPFTemplate template = XWPFTemplate.compile("D:/poitl/template.docx").render(map);
        FileOutputStream out = new FileOutputStream("D:/poitl/out_template.docx");
        template.write(out);
        out.flush();
        out.close();
        template.close();
    }
}
