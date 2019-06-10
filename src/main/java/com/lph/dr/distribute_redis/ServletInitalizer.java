package com.lph.dr.distribute_redis;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


/**
 * 项目部署外部tomcat时程序入口
 * @author: lph
 * @date:  2019/6/10 19:33
 * @version V1.0
 */
public class ServletInitalizer extends SpringBootServletInitializer {
    @Override
    protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(DistributeRedisApplication.class);
    }
}
