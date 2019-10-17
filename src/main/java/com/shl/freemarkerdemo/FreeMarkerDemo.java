package com.shl.freemarkerdemo;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.BufferedWriter;
import java.io.File;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerDemo {

    private static Configuration cfg = new Configuration();

    static {
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateLoader(new ClassTemplateLoader(FreeMarkerDemo.class, "/com/shl/freemarkerdemo"));

        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    public String processFtl2String(String templateFtl, Map map)
            throws Exception {

        if (templateFtl.contains(".")) {
            templateFtl = templateFtl.replaceAll("\\.", "/");
        }

        templateFtl += ".ftl";
        System.out.println("templateFtl:" + templateFtl);
        // 文件编码UTF-8
        Template template = cfg.getTemplate(templateFtl);
        template.setEncoding("UTF-8");

        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        template.process(map, writer);
        writer.flush();
        writer.close();

        return stringWriter.getBuffer().toString();
    }

    public static class VO implements Serializable{
        private String name;
        private String age;
        private String address;

        public VO() {
        }

        public VO(String name, String age, String address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static void main(String[] args) throws Exception {
        FreeMarkerDemo freeMarkerDemo = new FreeMarkerDemo();
        Map map = new HashMap();
        FreeMarkerDemo.VO vo = new VO("希望", "22", "中國");
        map.put("vo", vo);
        String result = freeMarkerDemo.processFtl2String("demo", map);
        System.out.println(result);

        File file = new File("");
    }
}

