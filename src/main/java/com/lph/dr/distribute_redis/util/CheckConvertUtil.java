package com.lph.dr.distribute_redis.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参数校验转换工具类
 *
 * @version V1.0
 * @author: lph
 * @date: 2019/6/14 15:13
 */
public class CheckConvertUtil {

    public static final Logger logger = LoggerFactory.getLogger(CheckConvertUtil.class);

    /**
     * 将对象转换为指定类型
     *
     * @param o   元数据
     * @param <T> 目的数据
     * @return 返回null时根据具体情况做判断
     */
    public static <T> T getConvertResult(Object o, Class<T> clz) {
        if (clz.isInstance(o)) {
            return clz.cast(o);
        } else if (String.class.isInstance(o)) {
            return JSONObject.parseObject(o.toString(), clz);
        }
        return null;
    }
}
