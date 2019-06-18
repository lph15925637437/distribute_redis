package com.lph.dr.distribute_redis.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lph.dr.distribute_redis.dto.TypeConvertDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;

@RestController
public class TestTypeConvert {

    @RequestMapping("/type")
    public String type(){
        TypeConvertDTO convertDTO = new TypeConvertDTO();

        System.err.println("转换后数据:" + JSON.toJSONString(convertDTO));
        try {
            String text = new ObjectMapper().writeValueAsString(convertDTO);
            System.err.println("text:" + text);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }
}
