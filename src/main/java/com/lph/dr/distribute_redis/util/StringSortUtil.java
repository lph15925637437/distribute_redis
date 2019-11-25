package com.lph.dr.distribute_redis.util;


import com.alibaba.fastjson.JSON;
import com.lph.dr.distribute_redis.dto.StringSortBean;
import com.lph.dr.distribute_redis.exception.DistributedErrorEnum;
import com.lph.dr.distribute_redis.exception.DistributedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序
 * @author: lph
 * @date:  2019/9/10 16:57
 * @version V1.0
 */
public class StringSortUtil {

    public static final Logger logger = LoggerFactory.getLogger(StringSortUtil.class);

    /**
     * @param type 1自然排序  2 按字符串字符长度进行排序  3 对对象进行排序(根据镇定字段)
     * @param list 待排序的内容
     */
    public static void sort(String type, List<String> list, List<?> objectList) {
        switch (type) {
            case "1":
                Collections.sort(list);
                break;
            case "2":
                list.sort(Comparator.comparingInt(String::length));
                break;
            case "3":
                Collections.sort(objectList, new StringComparatorUtil<Object>());
                break;
            default:
                logger.error("can not support sort method", type);
                throw new DistributedException(DistributedErrorEnum.INCORRECT_SORT_METHOD.getMessage());
        }
    }

    public static void main(String[] args){
//        List<String> list = new ArrayList<>();
//        list.add("welcome");
//        list.add("to");
//        list.add("java");
        List<StringSortBean> list = new ArrayList<>();
        list.add(new StringSortBean("14", "bauskj"));
        list.add(new StringSortBean("12", "causkj"));
        list.add(new StringSortBean("13", "aj"));
        System.err.println("sort前：" + JSON.toJSONString(list));
        sort("3", null, list);
        System.err.println("sort后：" + JSON.toJSONString(list));
    }
}
