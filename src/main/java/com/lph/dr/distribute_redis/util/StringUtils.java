package com.lph.dr.distribute_redis.util;

import com.google.common.base.*;
import com.google.common.collect.HashMultiset;
import com.lph.dr.distribute_redis.dto.StringSortBean;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {

    public static void main(String[] args) {
        isNullOrEmpty("1"); // 判断字符串是否为空

        common(); // 获取字符串中公共部分

        padStartOrEnd(4); // 字符串的填充

        splitter("[,，]{1,}"); // 字符串的拆分

        buildMapForSplitter("a=b;c=d,e=f"); // 二次拆分构建map（readonly）

        join(new String[]{"hello", "world"}); // 合并字符串底层是通过StringBuilder()操作

        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "b");
        map.put("c", "d");
        join(map);


        StringSortBean obj1 = null;
        StringSortBean obj2 = new StringSortBean("刘沛汉", "15925637437");
        StringSortBean obj3 = new StringSortBean("刘沛汉", "15925637437");
        equal(obj2, obj2);


        checkArgument("刘沛汉", 17, "若等666");

        HashMultiset.create();

    }

    public static void checkArgument(String name, Integer age, String desc) {
        Preconditions.checkNotNull(name, "name may not be null");
        Preconditions.checkArgument(age >= 18 && age < 99, "age must in range (18,99)");
        Preconditions.checkArgument(desc != null && desc.length() < 10, "desc too long, max length is ", 10);
    }

    public static void equal(Object a, Object b) {
        boolean aEqualsB = Objects.equal(a, b);
        System.err.println("\n" + aEqualsB);
    }

    public static void join(Map<String, String> map) {
        String mapJoinResult = Joiner.on(",").withKeyValueSeparator("=").join(map);
        System.out.println(mapJoinResult);
    }

    public static void join(String[] parts) {
        String joinResult = Joiner.on(" ").join(parts);
        System.out.println(joinResult);
    }

    public static void buildMapForSplitter(String splitString) {
        Map<String, String> kvs = Splitter.onPattern("[,;]{1,}").withKeyValueSeparator('=').split(splitString);
        for (Map.Entry<String, String> entry : kvs.entrySet()) {
            System.out.println(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
    }

    public static void splitter(String separatorPattern) {
        Iterable<String> splitResults = Splitter.onPattern(separatorPattern)
                .trimResults()
                .omitEmptyStrings()
                .split("hello,word,,世界，水平");

        for (String item : splitResults) {
            System.out.println("\n" + item);
        }
    }

    public static void padStartOrEnd(int minLength) {
        String padEndResult = Strings.padEnd("123", minLength, '0');
        System.out.println("\npadEndResult is " + padEndResult);
        String padStartResult = Strings.padStart("1", 2, '0');
        System.out.println("\npadStartResult is " + padStartResult);
    }

    public static void common() {
        String a = "com.jd.coo.Hello";
        String b = "com.jd.coo.Hi";
        String ourCommonPrefix = Strings.commonPrefix(a, b);
        System.out.println("\na,b common prefix is " + ourCommonPrefix);

        String c = "com.google.Hello";
        String d = "com.jd.a";
        String ourSuffix = Strings.commonSuffix(c, d);
        System.out.println("\nc,d common suffix is " + ourSuffix);
    }

    public static void isNullOrEmpty(String input) {
        boolean isNullOrEmpty = Strings.isNullOrEmpty(input);
        System.out.println("\ninput " + (isNullOrEmpty ? "is" : "is not") + " null or empty.");
    }
}
