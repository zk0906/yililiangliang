package com.roncoo.eshop.cache.controller;

import com.roncoo.eshop.cache.model.ProductInfo;
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

}
