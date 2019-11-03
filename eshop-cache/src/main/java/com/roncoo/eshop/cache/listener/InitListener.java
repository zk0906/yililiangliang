package com.roncoo.eshop.cache.listener;

import com.roncoo.eshop.cache.kafka.KafkaConsumer;
import com.roncoo.eshop.cache.spring.SpringContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * 系统初始化监听器
 * @author Administrator
 *
 */
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //获取Spring容器
        ServletContext sc = sce.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
        SpringContext.setApplicationContext(context);
        // kafka的初始化
        new Thread(new KafkaConsumer("cache-message")).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
