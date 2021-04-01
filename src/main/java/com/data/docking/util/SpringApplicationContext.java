package com.data.docking.util;

import org.springframework.context.ApplicationContext;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 上下文
 * @since 2021/4/1
 */
public class SpringApplicationContext {

    /**
     * spring容器
     */
    private static ApplicationContext context;

    /**
     * 构造函数
     *
     * @param context spring容器
     */
    public static void setApplicationContext(ApplicationContext context) {
        SpringApplicationContext.context = context;
    }

    /**
     * 获取bean
     *
     * @param clazz bean类型
     * @return bean实例
     */
    public static <T> T getBean(Class<? extends T> clazz) {
        return context.getBean(clazz);
    }

}
