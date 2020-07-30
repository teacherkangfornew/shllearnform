package com.shl.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberUtil {
    private static final Pattern DIGREG = Pattern.compile("(\\+?([0-9]+|[0-9]+\\.[0-9]+)|-?([0-9]+|[0-9]+\\.[0-9]+))");

    public  static  boolean  isNumber(String  num)  {
        if  (StringUtils.isBlank(num))  {
            return  false;
        }
        Matcher m  =  DIGREG.matcher(num);
        return  m.matches();

    }

    public  static  boolean  isFloat(String  num)  {
        return  isNumber(num);
    }


    public  static  boolean  isFloat(String  num,  Integer  floatnum)  {
        String  regx;
        if  (floatnum  ==  null)  {
            return  isNumber(num);
        }  else  if  (floatnum  ==  0)  {
            return  NumberUtils.isDigits(num);
        }  else  if  (floatnum  ==  1)  {
            regx  =  "(\\+?([0-9]+|[0-9]+\\.[0-9])|-?([0-9]+|[0-9]+\\.[0-9]))";
        }  else  {
            regx  =  "(\\+?([0-9]+|[0-9]+\\.[0-9]{1,"  +  floatnum  +  "})|-?([0-9]+|[0-9]+\\.[0-9]{1,"  +  floatnum  +  "}))";
        }
        return  Pattern.compile(regx).matcher(num).matches();
    }
}
