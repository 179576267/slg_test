package com.douqu.game.main.server;


import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.main.msg.AMsgChannel;
import com.douqu.game.main.platform.PlatformSDK;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

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

    public static TaskExecutor getTaskExecutor()
    {
        return SpringContext.getBean(ConstantFactory.TASK_EXECUTOR_NAME);
    }

    public static AMsgChannel getMessageChannel(Class cls){
        return SpringContext.getBean(cls);
    }
    public static PlatformSDK getPlatformSDK(Class cls){
        return SpringContext.getBean(cls);
    }

    public static void stop()
    {
        SpringApplication.exit(applicationContext);
    }

    public static String getProfile()
    {
        Environment env = getBean(Environment.class);
        return env.getProperty("profile");
    }
}
