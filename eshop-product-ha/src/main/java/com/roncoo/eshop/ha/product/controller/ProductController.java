package com.roncoo.eshop.ha.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductController {

    @RequestMapping("/getProductInfo")
    @ResponseBody
    public String hello(String productId){
        return "productId=," + productId + " product:apple";
    }

}
