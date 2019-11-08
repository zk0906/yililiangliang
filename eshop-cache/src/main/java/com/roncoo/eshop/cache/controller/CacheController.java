package com.roncoo.eshop.cache.controller;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.model.ShopInfo;
import com.roncoo.eshop.cache.rebuild.RebuildCacheQueue;
import com.roncoo.eshop.cache.service.CacheService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 缓存Controller
 * @author Administrator
 *
 */
@Controller
public class CacheController {
    @Resource
    private CacheService cacheService;

    @RequestMapping("/index")
    @ResponseBody
    public String index(){
        return "index";
    }

    @RequestMapping("testPutCache")
    @ResponseBody
    public String testPutCache(ProductInfo productInfo){
        cacheService.saveLocalCache(productInfo);
        return "success";
    }

    @RequestMapping("testGetCache")
    @ResponseBody
    public ProductInfo testGetCache(Long id){
        ProductInfo productInfo = cacheService.getLocalCache(id);
        return  productInfo;
    }

    /**
     * 本节是测试nginx应用层  能否能从后端应用中拿到缓存
     *
     * 被动重建：
     * 当本地和redis都没有数据时，需要直接读取源头数据，直接返回给nginx，同时推送一条消息到一个队列，后台线程异步消费，此处也需要加入分布式锁的处理
     * @param productId
     * @return
     */
    @RequestMapping("getProductInfo")
    @ResponseBody
    public ProductInfo getProductInfo(Long productId){
        ProductInfo productInfo = null;
        //先从redis中获取数据
        productInfo = cacheService.getProductInfoFromRedisCache(productId);
        if(productInfo == null){
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
        }
        if(productInfo == null){
            //需要重建缓存,模拟数据库的一条返回，注意其修改时间
            String productInfoJSON = "{\"id\": 2, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modified_time\": \"2017-01-01 12:01:00\"}";
            productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
            // 将数据推送到一个内存队列中
            RebuildCacheQueue queue = RebuildCacheQueue.getInstance();
            queue.putProductInfo(productInfo);
        }
        return productInfo;
    }

    @RequestMapping("getShopInfo")
    @ResponseBody
    public ShopInfo getShopInfo(Long shopId){
        ShopInfo shopInfo = null;
        //先从redis中获取数据
        shopInfo = cacheService.getShopInfoFromRedisCache(shopId);
        if(shopInfo == null){
            shopInfo = cacheService.getShopInfoFromLocalCache(shopId);
        }
        if(shopInfo == null){
            //需要重建缓存
//            productInfo = cacheService.getProductInfoFromLocalCache(productId);
        }
        return shopInfo;
    }
}
