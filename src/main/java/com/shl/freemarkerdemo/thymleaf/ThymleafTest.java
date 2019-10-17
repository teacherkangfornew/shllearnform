package com.shl.freemarkerdemo.thymleaf;

import com.sun.org.apache.xerces.internal.xs.ItemPSVI;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.*;

public class ThymleafTest {

    public static String processThyemleaf(List dataList, String template) {
        if (template == null || "".equals(template.trim())) {
            return "";
        }
        StringTemplateResolver stringres=new StringTemplateResolver();
        stringres.setTemplateMode(TemplateMode.HTML);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(stringres);
        Context context = new Context();
        context.setVariable("dataList", dataList);
        return templateEngine.process(template, context);

    }

    public static void main(String[] args) {
        StringTemplateResolver stringres=new StringTemplateResolver();
        stringres.setTemplateMode(TemplateMode.HTML);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(stringres);
        Context context = new Context();
        List<Map> dataList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Map item = new HashMap();
            item.put("text", "希望" + i);
            dataList.add(item);
        }
        Map itemMap = new HashMap();
        itemMap.put("111", "111");
        itemMap.put("222", "222");
        itemMap.put("333", "333");

        Map itemMap2 = new HashMap();
        itemMap2.put("111", "shen");
        itemMap2.put("222", "shao");
        itemMap2.put("333", "ye");
        String template = "<div  th:each=\"obj,iter : ${dataList}\"><p th:text=\"${iter.current.key}\">1</p><p th:text=\"${iter.current.value}\"></p></div>";
        context.setVariable("dataList", itemMap);
        context.setVariable("dataList2", itemMap2);
        template = "<div th:each=\"obj,iter : ${dataList}\"><p th:text=\"${dataList2[iter.current.key]}\">1</p></div>";


        String[] idArr = {"111", "222", "333"};
        context.setVariable("dataList3", idArr);
        template = "<div th:each=\"obj,iter : ${dataList2}\"><p th:text=\"${dataList3[iter.index]}\">1</p></div>";
        List res = new LinkedList();
        res.add("111");
        res.add("222");
        res.add("333");
        context.setVariable("dataList4", res);
        template = "<div th:if=\"${dataList4 != null}\" th:each=\"obj,iter : ${dataList2}\"><p th:if=\"${dataList2 != null}\" th:text=\"${dataList4[iter.index]}\">1</p></div>";
        String resolverResult = templateEngine.process(template, context);
        System.out.println(resolverResult);



    }
}
