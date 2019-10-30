package com.roncoo.eshop.cache.listener;

import com.roncoo.eshop.cache.kafka.KafkaConsumer;

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
        // kafka的初始化
//        RequestProcessorThreadPool.init();
        System.out.println("系统初始化监听器");
        new Thread(new KafkaConsumer("cache-message")).start();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
