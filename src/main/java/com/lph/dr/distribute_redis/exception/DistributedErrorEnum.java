package com.lph.dr.distribute_redis.exception;


/**
 * 自定义分布式枚举类
 * @author: lph
 * @date:  2019/6/11 17:14
 * @version V1.0
 */
public enum DistributedErrorEnum {
    D_LOCK("D10000", "加锁失败!"),
    D_UNLOCK("D10000", "解锁失败!"),
    ;
    private String errorCode;
    
    private String message;

    DistributedErrorEnum(String code,String message){
        this.errorCode=code;
        this.message=message;
    }

    public String getMessage(){
        return"[code="+errorCode+",message="+message+"]";
    }
    public String getErrorCode() {
    
        return errorCode;
    }
    
    public String getMsg(){
        return message;
    }
    
    
    
    
    
    
}
  
 