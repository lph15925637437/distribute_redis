package com.lph.dr.distribute_redis.exception;

import org.springframework.util.StringUtils;


/**
 * 自定义分布式异常类
 * @author: lph
 * @date:  2019/6/11 17:13
 * @version V1.0
 */
public class DistributedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
     private String code;
     
     private String message;
     
     
    public DistributedException(){
        
    }
    
    public DistributedException(String message){
        super(message);
    }
    
    public  DistributedException(String errorCode,String message){
        super(message);
        this.code=errorCode;
    }
    
    public  DistributedException(DistributedErrorEnum errorEnum){
        super(errorEnum.getMessage());
        this.code=errorEnum.getErrorCode();
        this.message=errorEnum.getMsg();
        
    }
    
    public  DistributedException(DistributedErrorEnum errorEnum,String message){
        super(errorEnum.getMessage()+message);
        this.code=errorEnum.getErrorCode();
        this.message=message;
    }
    
    public  DistributedException(DistributedErrorEnum errorEnum,Throwable e){
        super(e);
        this.code=errorEnum.getErrorCode();
        this.message=errorEnum.getMsg();
    }

    public String getErrorCode() {
        if(StringUtils.isEmpty(code)){
            return "10001";
        }
        return code;
    }

    public String getMessage() {
        if(StringUtils.isEmpty(message)){
           return super.getMessage(); 
        }
        return message;
    }

    public DistributedException(String message, Throwable cause) {
        super(message, cause);
        this.message=message;
    }
}