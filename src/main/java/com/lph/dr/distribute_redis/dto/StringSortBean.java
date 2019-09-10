package com.lph.dr.distribute_redis.dto;


import com.lph.dr.distribute_redis.enums.StringComparator;

/**
 * 测试根据某个字段对对象进行排序
 * @author: lph
 * @date:  2019/9/10 16:50
 * @version V1.0
 */
public class StringSortBean {

    @StringComparator
    private String name;

    private String phone;

    public StringSortBean() {
    }

    public StringSortBean(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
