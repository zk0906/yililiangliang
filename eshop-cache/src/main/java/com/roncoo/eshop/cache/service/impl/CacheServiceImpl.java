package com.roncoo.eshop.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.cache.dao.RedisDAO;
import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.model.ShopInfo;
import com.roncoo.eshop.cache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

/**
 * 缓存Service实现类
 * @author Administrator
 *
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService {
    private final String CACHE_NAME = "local";
    @Resource
    private RedisDAO redisDao;
    @Resource
    private JedisCluster jedisCluster;


    @CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    public ProductInfo saveLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    public ProductInfo getLocalCache(Long id) {
        return null;
    }

    @CachePut(value = CACHE_NAME, key = "'product_info_'+#productInfo.getId()")
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Cacheable(value = CACHE_NAME, key = "'product_info_'+#productInfo")
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        return null;
    }

    @Override
    public ProductInfo getProductInfoFromRedisCache(Long productId) {
        ProductInfo productInfo = null;
        System.out.println("从redis中获取缓存，productId="+productId+"   开始");
        String key = "product_info_" + productId;
        // todo redis的集群有待解决
//        String json = jedisCluster.get(key);
        String json = "{\"id\": 1, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1}";
        System.out.println("从redis中获取缓存，productId="+productId+"   结束  返回："+ json);
        if(json != null && !json .equals("")) {
            productInfo = JSONObject.parseObject(json,ProductInfo.class);
        }
        return productInfo;
    }

    @Override
    public ShopInfo getShopInfoFromRedisCache(Long shopId) {
        ShopInfo shopInfo = null;
        System.out.println("从redis中获取缓存，shopId="+shopId+"   开始");
        String key = "shop_info_" + shopId;
//        String json = jedisCluster.get(key);
        String json = "{\"id\": 1, \"name\": \"小王的手机店\", \"level\": 5, \"goodCommentRate\":0.99}";
        System.out.println("从redis中获取缓存，shopId="+shopId+"   结束  返回："+ json);
        if(json != null && !json .equals("")) {
            shopInfo = JSONObject.parseObject(json,ShopInfo.class);
        }
        return shopInfo;
    }

    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo) {
        return null;
    }

    @Cacheable(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    @Override
    public void saveProductInfo2ReidsCache(ProductInfo productInfo) {
        String key = "product_info_" + productInfo.getId();
        String productInfoJSON = JSONObject.toJSONString(productInfo);
        redisDao.set(key,productInfoJSON);
    }

    @Override
    public void saveShopInfo2ReidsCache(ShopInfo shopInfo) {
        String key = "shop_info_" + shopInfo.getId();
        String shopInfoJSON = JSONObject.toJSONString(shopInfo);
        redisDao.set(key,shopInfoJSON);

    }
}
