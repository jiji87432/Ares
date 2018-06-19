package com.pay.risk.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring上下文
 * 
 * 
 * @author jiude.sun
 * @version [版本号, 2014-8-14]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component
public class SpringContextUtil implements ApplicationContextAware
{
    
    private static ApplicationContext applicationContext; // Spring应用上下文环境
    
    /*
     * 
     * 实现了ApplicationContextAware 接口，必须实现该方法；
     * 
     * 通过传递applicationContext参数初始化成员变量applicationContext
     */
    
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        SpringContextUtil.applicationContext = applicationContext;
    }
    
    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name)
        throws BeansException
    {
        return (T)applicationContext.getBean(name);
    }
    
}
