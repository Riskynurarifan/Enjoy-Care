package com.enjoycare.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {

    private static final ApplicationContext CONTEXT =
        new ClassPathXmlApplicationContext("applicationContext.xml");

    public static <T> T getBean(Class<T> beanClass) {
        return CONTEXT.getBean(beanClass);
    }
}
