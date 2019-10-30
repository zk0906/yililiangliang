package com.roncoo.eshop.inventory.thread;


import com.roncoo.eshop.inventory.request.Request;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 执行请求的工作线程
 * @author Administrator
 *
 */
public class RequestProcessorThread implements Callable<Boolean> {

    /**
     * 自己监控的内存队列
     */
    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {
        System.out.println("===========日志===========: 工作线程处理请求 call方法");
        try{
            while (true){
                System.out.println("===========日志===========: 工作线程处理请求 call方法   into");
                // ArrayBlockingQueue
                // Blocking就是说明，如果队列满了，或者是空的，那么都会在执行操作的时候，阻塞住，实现了读写的串行化
                Request request = queue.take();
                System.out.println("===========日志===========: 开始工作线程处理请求，商品id=" + request.getProductId());
                // 执行这个request操作
                request.process();
                System.out.println("===========日志===========: 结束工作线程处理请求，商品id=" + request.getProductId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
