package com.lph.dr.distribute_redis.util;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 时间转换工具类
 * @author: lph
 * @date:  2019/6/13 8:52
 * @version V1.0
 */
public class DateUtils {

    public  static final String format(Date date, String pattern){
        if(date!=null){
            SimpleDateFormat sdf=new SimpleDateFormat(pattern);
            return sdf.format(date);
        }else{
            return null;
        }

    }
}
