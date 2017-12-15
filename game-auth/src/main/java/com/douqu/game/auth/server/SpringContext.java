package com.douqu.game.auth.server;

import com.douqu.game.auth.platform.PlatformSDK;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by bean
 * 2017-03-23 16:58.
 */
public class SpringContext
{
    /**
     * spring管理器
     */
    private static ApplicationContext applicationContext;

    public static void init(ApplicationContext ac)
    {
        applicationContext = ac;
    }

    public static <T> T getBean(Class cls)
    {
        return (T) applicationContext.getBean(cls);
    }

    public static <T> T getBean(String name)
    {
        return (T) applicationContext.getBean(name);
    }

    public static PlatformSDK getPlatformSDK(Class cls){
        return SpringContext.getBean(cls);
    }

    public static void stop()
    {
        SpringApplication.exit(applicationContext);
    }
}
