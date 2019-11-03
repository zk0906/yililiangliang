package com.roncoo.eshop.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.cache.dao.RedisDAO;
import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.model.ShopInfo;
import com.roncoo.eshop.cache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
