package com.lph.dr.distribute_redis.enums;

public enum GenderEnum {

    Unknown("0", "未知"),
    Male("1", "男性"),
    Female("2", "女性");
    
    private String sex;
    
    private String desc;
    
    private GenderEnum(String sex, String desc){
        
        this.sex = sex;
        this.desc = desc;
    }

    public String getSex() {
        return sex;
    }

    public String getDesc() {
        return desc;
    }
}