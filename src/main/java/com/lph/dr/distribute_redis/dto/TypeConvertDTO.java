package com.lph.dr.distribute_redis.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lph.dr.distribute_redis.util.ChangeRetDataStatusSerialize;

import java.util.Date;

public class TypeConvertDTO{

    private String name = "lph";

    private String phone = "156987452145";

    @JsonSerialize(using = ChangeRetDataStatusSerialize.ProcessDoubleSerialize.class)
    private Double buybackAmount = 3.1415926;

    @JsonSerialize(using = ChangeRetDataStatusSerialize.ProcessDateSerialize.class)
    private Date createTime = new Date();

    @JsonSerialize(using = ChangeRetDataStatusSerialize.processIntegerSerializer.class)
    private int status = 0;

    public Double getBuybackAmount() {
        return buybackAmount;
    }

    public void setBuybackAmount(Double buybackAmount) {
        this.buybackAmount = buybackAmount;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
