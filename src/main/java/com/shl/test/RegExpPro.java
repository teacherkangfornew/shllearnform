package com.shl.test;

import javassist.runtime.Desc;
import sun.security.krb5.internal.crypto.Des;

import java.util.regex.Pattern;

public class RegExpPro {

    private static final String DESC = "匹配结果 = ";

    public static void main(String[] args) {
        characterClassNotDesc();


    }

    /**
     * 验证字符组在没有任何修饰的情况下
     * 只占据一个位置，而且必须配置一个位置
     */
    static void characterClassNotDesc() {
        String str = "1";
        System.out.println(DESC + str.matches("[123]"));
        str = "a1";
        System.out.println(DESC + str.matches("[123]"));
        System.out.println(DESC + p.matcher(str).find());
        str = "a";
        System.out.println(DESC + p.matcher(str).find());
        str = "aa嗷嗷3";
        System.out.println(DESC + p.matcher(str).find());

    }

    static Pattern p = Pattern.compile("[123]");
}
