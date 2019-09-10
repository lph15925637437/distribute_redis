package com.lph.dr.distribute_redis.exception;


/**
 * 自定义分布式枚举类
 * @author: lph
 * @date:  2019/6/11 17:14
 * @version V1.0
 */
public enum DistributedErrorEnum {
    D_LOCK("D10000", "加锁失败!"),
    D_UNLOCK("D10001", "解锁失败!"),
    API_REQUEST_TOO_MUCH("10001", "请求过于频繁!!"),
    BAD_REQUEST("10002", "请查看redis是否开启!!"),
    BAD_SORT("10003", "排序失败!!"),
    INCORRECT_SORT_METHOD("10004", "不正常的排序方式!!"),
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
  
 