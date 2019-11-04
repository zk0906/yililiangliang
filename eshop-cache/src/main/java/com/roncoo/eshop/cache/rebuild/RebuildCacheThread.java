package com.roncoo.eshop.cache.rebuild;

import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.service.CacheService;
import com.roncoo.eshop.cache.spring.SpringContext;
import com.roncoo.eshop.cache.zk.ZookeeperSession;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 缓存重建线程
 */
public class RebuildCacheThread implements Runnable{
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        System.out.println("run ....");
        RebuildCacheQueue queue =RebuildCacheQueue.getInstance();
        ZookeeperSession zkSession = ZookeeperSession.getInstance();
        CacheService cacheService = (CacheService)SpringContext.getApplicationContext().getBean("cacheService");
        while(true){
            System.out.println("while 1");
            ProductInfo productInfo = queue.takeProduct();
            System.out.println("while 2");

            // 加代码，在将数据直接写入redis缓存之前，应该先获取一个zk的分布式锁
            zkSession.acquireDistributedLock(productInfo.getId());

            // 获取到了锁
            // 先从redis中获取数据
            ProductInfo existedProductInfo = cacheService.getProductInfoFromRedisCache(productInfo.getId());
            if(existedProductInfo != null) {
                // 比较当前数据的时间版本比已有数据的时间版本是新还是旧
                try {
                    Date date = sdf.parse(productInfo.getModifiedTime());
                    Date existedDate = sdf.parse(existedProductInfo.getModifiedTime());

                    if(date.before(existedDate)) {
                        System.out.println("current date[" + productInfo.getModifiedTime() + "] is before existed date[" + existedProductInfo.getModifiedTime() + "]");
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("current date[" + productInfo.getModifiedTime() + "] is after existed date[" + existedProductInfo.getModifiedTime() + "]");
            } else {
                System.out.println("existed product info is null......");
            }

            cacheService.saveProductInfo2ReidsCache(productInfo);

            // 释放分布式锁
            zkSession.releaseDistributedLock(productInfo.getId());
        }
    }
}
