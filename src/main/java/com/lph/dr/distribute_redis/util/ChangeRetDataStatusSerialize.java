package com.lph.dr.distribute_redis.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

public class ChangeRetDataStatusSerialize {

    /**
     *  处理double类型数据
     */
    public static class ProcessDoubleSerialize extends JsonSerializer<Double> {
        //原本这里是  ##.00 ,带来的问题是如果数据库数据为0.00返回“ .00 “经评论指正，改为0.00
        private DecimalFormat df = new DecimalFormat("0.00");
        @Override
        public void serialize(Double arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
            if(arg0 != null) {
                arg1.writeString(df.format(arg0));
            }
        }
    }

    /**
     *  处理返回时间类型的数据
     */
    public static class ProcessDateSerialize extends JsonSerializer<Date> {

        @Override
        public void serialize(Date arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {
            if (arg0 != null) {
                arg1.writeString(DateUtils.format(arg0, "yyyy-MM-dd HH:mm:ss"));
            }
        }
    }

    public static class processIntegerSerializer extends JsonSerializer<Integer> {
        @Override
        public void serialize(Integer arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {
            String statusStr = "";
            switch (arg0) {
                case 0:
                    statusStr = "新建状态";
                    break;
                case 1:
                    statusStr = "就绪状态";
                    break;
                case 2:
                    statusStr = "运行状态";
                    break;
                case 3:
                    statusStr = "阻塞和唤醒线程";
                    break;
                case 4:
                    statusStr = " 死亡状态";
                    break;
                default:
                    statusStr = "状态信息不符合";
            }
            arg1.writeString(statusStr);
        }
    }
}
