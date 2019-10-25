package com.roncoo.eshop.inventory.service.impl;

import com.roncoo.eshop.inventory.mapper.UserMapper;
import com.roncoo.eshop.inventory.model.User;
import com.roncoo.eshop.inventory.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户Service实现类
 * @author Administrator
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
//	@Resource
//	private RedisDAO redisDAO;

	@Override
	public User findUserInfo() {
		return userMapper.findUserInfo();
	}

	@Override
	public User getCachedUserInfo() {
//		redisDAO.set("cached_user_lisi", "{\"name\": \"lisi\", \"age\":28}");
//
//		String userJSON = redisDAO.get("cached_user_lisi");
//		JSONObject userJSONObject = JSONObject.parseObject(userJSON);
//
		User user = new User();
//		user.setName(userJSONObject.getString("name"));
//		user.setAge(userJSONObject.getInteger("age"));
//
		return user;
	}

}
