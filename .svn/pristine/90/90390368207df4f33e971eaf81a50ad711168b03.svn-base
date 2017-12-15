package com.douqu.game.auth;

import com.douqu.game.auth.server.SpringContext;
import com.douqu.game.core.factory.LoadFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Author : wangzhenfei
 * 2017-07-06 15:36
 */
@EnableTransactionManagement//事务支持,要用事务控制的方法里必须抛出异常才会回滚
@SpringBootApplication
@MapperScan("com.douqu.game.auth.database.mapper")//扫描mapper
public class StartServer {

    public static void main(String[] args)
    {
        ApplicationContext applicationContext =  SpringApplication.run(StartServer.class);
        SpringContext.init(applicationContext);

        LoadFactory.loadConfig();
    }



}
