package com.data.docking.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 属性配置读取
 * @since 2021/4/1
 */
@Configuration
@Data
public class PropertiesConfig {

    @Value("${oss.ip}")
    private String ossIp;

    @Value("${common.http.defaultMaxPerRoute}")
    private Integer httpDefaultMaxPerRoute;

    @Value("${common.http.maxTotal}")
    private Integer httpMaxTotal;

    @Value("${common.http.defaultKeepTime}")
    private Integer httpDefaultKeepTime;

    @Value("${common.http.defaultTimeout}")
    private Integer httpDefaultTimeOut;



}
