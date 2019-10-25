package com.roncoo.eshop.inventory.controller;

import com.roncoo.eshop.inventory.model.User;
import com.roncoo.eshop.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 商品库存Controller
 * @author Administrator
 *
 * 大家考虑一下，我要模拟的场景：
 *
 *（1）一个更新商品库存的请求过来，然后此时会先删除redis中的缓存，然后模拟卡顿5秒钟
 *（2）在这个卡顿的5秒钟内，我们发送一个商品缓存的读请求，因为此时redis中没有缓存，就会来请求将数据库中最新的数据刷新到缓存中
 *（3）此时读请求会路由到同一个内存队列中，阻塞住，不会执行
 *（4）等5秒钟过后，写请求完成了数据库的更新之后，读请求才会执行
 *（5）读请求执行的时候，会将最新的库存从数据库中查询出来，然后更新到缓存中
 *
 * 如果是不一致的情况，可能会出现说redis中还是库存为100，但是数据库中也许已经更新成了库存为99了
 *
 * 现在做了一致性保障的方案之后，就可以保证说，数据是一致的
 *
 * 最后说一点点
 *
 * 包括这个方案在内，还有后面的各种解决方案，首先都是针对我自己遇到过的特殊场景去设计的
 *
 * 可能这个方案就不一定完全100%适合其他的场景，也许还要做一些改造才可以，本来你学习一个课程，它就不是万能的，你可能需要嚼烂了，吸收了，改造了，才能应用到自己的场景中
 *
 * 另外一个，也有一种可能，就是说方案比较复杂，即使我之前做过，也许有少数细节我疏忽了，没有在课程里面讲解，导致解决方案有一些漏洞或者bug
 *
 * 我讲解方案，主要是讲解架构思想，或者是设计思想，技术思想，有些许漏洞，希望大家谅解
 *
 * 课程真正最重要的，不是给你一套100%包打天下的代码，而是告诉一种设计思想，多种设计思想组合起来，就是某种架构思想
 *
 */
@Controller
public class ProductInventoryController {
    @Autowired
    private UserService userService;

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
}
