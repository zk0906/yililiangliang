package com.roncoo.eshop.inventory.controller;

import com.roncoo.eshop.inventory.dao.RedisDAO;
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
    @Resource
    private RedisDAO redisDAO;

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

    @RequestMapping("/jedis/test/add")
    @ResponseBody
    public void jedisTest1(String id,String val) {
        if(id.equals("bath")){
            System.out.println("测试批量插入数据");
            for(int i = 1; i < 100; i++){
                String id1 = System.currentTimeMillis() +"";
                System.out.println("id ="+id1);
                jedisCluster.set(id1,id1);
            }
        }else {
            System.out.println("测试插入单个数据  id=" + id + " val=" + val);
            jedisCluster.set(id,val);
        }
    }

    @RequestMapping("/jedis/test/del")
    @ResponseBody
    public void jedisTest2(String id) {
        Long result = jedisCluster.del(id);
        System.out.println("测试删除数据 id=" + id + " result=" + result);
    }

    @RequestMapping("/jedis/test/get")
    @ResponseBody
    public void jedisTest3(String id) {
        String val = jedisCluster.get(id);
        System.out.println("测试获得数据 id=" + id + " val=" + val);
    }

    @RequestMapping("/jedis/autoTest")
    @ResponseBody
    public void jedisAutoTest(String id) {

    }

    @RequestMapping("/redis/test/add")
    @ResponseBody
    public void redisTest1(String id,String val) {
        if(id.equals("bath")){
            System.out.println("redis测试批量插入数据");
            for(int i = 0; i < 1000; i++){
                String id1 = "test:"+i+":"+System.currentTimeMillis();
                System.out.println("id ="+id1);
                redisDAO.set(id1,id1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else {
            System.out.println("redis测试插入单个数据  id=" + id + " val=" + val);
            redisDAO.set(id,val);
        }
    }

    @RequestMapping("/redis/test/del")
    @ResponseBody
    public void redisTest2(String id) {
        System.out.println("开始redis测试删除数据 id=" + id);
        redisDAO.delete(id);
        System.out.println("redis测试删除数据 id=" + id);
    }

    @RequestMapping("/redis/test/get")
    @ResponseBody
    public void redisTest3(String id) {
        System.out.println("开始redis测试获得数据 id=" + id);
        String val = redisDAO.get(id);
        System.out.println("redis测试获得数据 id=" + id + " val=" + val);
    }
}
