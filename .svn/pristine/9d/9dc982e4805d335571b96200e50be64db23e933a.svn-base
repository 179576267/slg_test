package com.douqu.game.main;


import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.main.listener.ControllerListener;
import com.douqu.game.main.server.SpringContext;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Author : Bean
 * 2017-07-06 15:36
 */
@EnableTransactionManagement//事务支持,要用事务控制的方法里必须抛出异常才会回滚
@MapperScan("com.douqu.game.main.database.mapper")//扫描mapper
@EnableScheduling//定时任务
@SpringBootApplication
public class SpringServer {

    public static void start()
    {
        ApplicationContext applicationContext = SpringApplication.run(SpringServer.class, new String[]{});
        SpringContext.init(applicationContext);

    }





















///////////////////////////////////////////////////////////////////////////////////////////////////


    @ConfigurationProperties(prefix = "executor")//读取配置文件里前缀为executor的所有配置
    class BeanFactory
    {
        /** Set the ThreadPoolExecutor's core pool size. */
        private int corePoolSize;
        /** Set the ThreadPoolExecutor's maximum pool size. */
        private int maxPoolSize;
        /** Set the capacity for the ThreadPoolExecutor's BlockingQueue. */
        private int queueCapacity;

        @Bean(ConstantFactory.TASK_EXECUTOR_NAME)
        public TaskExecutor taskExecutor()
        {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(corePoolSize);
            executor.setMaxPoolSize(maxPoolSize);
            executor.setQueueCapacity(queueCapacity);
            executor.setThreadNamePrefix("TaskExecutor-");

            // rejection-policy：当pool已经达到max size的时候，如何处理新任务
            // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            executor.initialize();
            return executor;
        }

        /**
         * 过滤器
         * @return
         */
        @Bean
        public FilterRegistrationBean indexFilterRegistration() {
            FilterRegistrationBean registration = new FilterRegistrationBean(new ControllerListener());
            registration.addUrlPatterns("/*");
            return registration;
        }

        /**
         * 跨域
         * @return
         */
        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurerAdapter() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOrigins("*")
                            .allowCredentials(true)
                            .allowedMethods("GET","POST")
                            .maxAge(3600)
                            .allowedHeaders("*");
                }
            };
        }

        /**
         * 国际化
         * @return
         */
        @Bean
        public ResourceBundleMessageSource messageSource() {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setUseCodeAsDefaultMessage(true);
            messageSource.setFallbackToSystemLocale(false);
            messageSource.setBasename("application");
            messageSource.setDefaultEncoding("UTF-8");
            messageSource.setCacheSeconds(2);
            return messageSource;
        }



        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }


    }
}
