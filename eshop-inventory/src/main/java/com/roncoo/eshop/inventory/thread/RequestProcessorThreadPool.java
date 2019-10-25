package com.roncoo.eshop.inventory.thread;

import com.roncoo.eshop.inventory.request.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 请求处理线程池：单例
 * @author Administrator
 *
 */
public class RequestProcessorThreadPool {

    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<ArrayBlockingQueue<Request>>();

    /**
     * 线程池
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);
    public RequestProcessorThreadPool() {
        for(int i = 0; i < 10; i++){
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(100);
            queues.add(queue);
        }

    }

    /**
     * 单例有很多种方式去实现：采取绝对线程安全的一种方式
     *
     * 静态内部类的方式，去初始化单例
     *
     * @author Administrator
     *
     */
    private static class Singleton{
        private static RequestProcessorThreadPool instance;

        //静态块 只执行一次
        static {
            instance = new RequestProcessorThreadPool();
        }

        public static  RequestProcessorThreadPool getInstance(){
            return instance;
        }
    }

    /**
     * jvm的机制去保证多线程并发安全
     *
     * 内部类的初始化，一定只会发生一次，不管多少个线程并发去初始化
     *
     * @return
     */
    public static  RequestProcessorThreadPool getInstance(){
        return Singleton.getInstance();
    }

    public static void init(){

    }
}
