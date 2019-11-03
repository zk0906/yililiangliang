package com.roncoo.eshop.cache.controller;

import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.model.ShopInfo;
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
            //需要重建缓存
//            productInfo = cacheService.getProductInfoFromLocalCache(productId);
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
