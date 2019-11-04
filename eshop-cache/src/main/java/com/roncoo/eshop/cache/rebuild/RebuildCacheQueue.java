package com.roncoo.eshop.cache.rebuild;

import com.roncoo.eshop.cache.model.ProductInfo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 重建缓存的内存队列
 */
public class RebuildCacheQueue {
    private ArrayBlockingQueue<ProductInfo> queue = new ArrayBlockingQueue<>(1000);

    public void putProductInfo(ProductInfo productInfo){
        try {
            queue.put(productInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ProductInfo takeProduct(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 内部单例类
     * @author Administrator
     *
     */
    private static class SingleTon{
        private static RebuildCacheQueue instance;
        static {
            instance = new RebuildCacheQueue();
        }
        public static RebuildCacheQueue getInstance(){
            return instance;
        }
    }

    public static RebuildCacheQueue getInstance(){
        return SingleTon.getInstance();
    }

    public static void init(){
        getInstance();
    }


}
