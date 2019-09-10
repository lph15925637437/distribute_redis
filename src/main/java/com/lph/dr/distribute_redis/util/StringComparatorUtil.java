package com.lph.dr.distribute_redis.util;


import com.lph.dr.distribute_redis.enums.StringComparator;
import com.lph.dr.distribute_redis.exception.DistributedErrorEnum;
import com.lph.dr.distribute_redis.exception.DistributedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * 比较器工具类,可以进行对象中的元素进行比较排序
 *
 * @version V1.0
 * @author: lph
 * @date: 2019/5/22 14:45
 */
public class StringComparatorUtil<T> implements Comparator {

    public static final Logger logger = LoggerFactory.getLogger(StringComparatorUtil.class);

    /**
     * 根据对象的属性对对象进行排序
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Object o1, Object o2) throws DistributedException {
        String value1, value2 = null;
        int minNum = 0;
        try {
            value1 = processData(o1);
            value2 = processData(o2);
            minNum = value1.compareTo(value2);
        } catch (IllegalAccessException e) {
            logger.error("在进行字符串数据比较时比较字段中的值为空:{}", e.getMessage());
            throw new DistributedException(DistributedErrorEnum.BAD_SORT.getMessage());
        }

        return minNum;
    }

    private String processData(Object object) throws IllegalAccessException {
        Class objClass = object.getClass();
        Field[] fields = objClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(StringComparator.class)) {
                return (String) field.get(object);
            }
        }

        return null;
    }
}
