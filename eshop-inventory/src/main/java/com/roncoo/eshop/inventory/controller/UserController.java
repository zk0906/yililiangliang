package com.roncoo.eshop.inventory.controller;

import com.roncoo.eshop.inventory.model.User;
import com.roncoo.eshop.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    //直接测试jedis
    @Resource
    private JedisCluster jedisCluster;

    @RequestMapping("/getCachedUserInfo")
    @ResponseBody
    public User getCachedUserInfo(){
        User user = userService.getCachedUserInfo();
        return user;
    }

    @RequestMapping("/getUserInfo")
    @ResponseBody
    public User getUserInfo() {
        User user = userService.findUserInfo();
        return user;
    }

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "Hello World";
    }

    @RequestMapping("/jedisTest1")
    @ResponseBody
    public void jedisTest1(String id,String val) {
        jedisCluster.set(id,val);
    }

}
