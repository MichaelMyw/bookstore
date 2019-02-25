package com.mjava.bookstore.user.service;

import com.mjava.bookstore.user.dao.UserDao;
import com.mjava.bookstore.user.domain.User;

public class UserService {
	private UserDao userDao = new UserDao();
	
	/**
	 * 注册功能
	 * @param form
	 * @throws UserException 
	 */
	public void regist(User form) throws UserException {
		
		//校验用户名
		User user = userDao.findByUsername(form.getUsername());
		if(user != null) {
			throw new UserException("用户名已被注册");
		}
		
		//校验Email
		user = userDao.findByEmail(form.getEmail());
		if(user != null) {
			throw new UserException("邮箱已被使用");
		}
		
		userDao.add(form);
	}
	
	/**
	 * 激活功能
	 * @param code
	 * @throws UserException 
	 */
	public void active(String code) throws UserException {
		//使用code查询数据库，得到user
		User user = userDao.findUserByCode(code);
		//如果user不存在，说明激活码不存在，抛出异常
		if(user == null) {
			throw new UserException("激活码无效");	
		}
		
		//校验用户的状态是否为未激活状态，如果已激活，说明是二次激活抛出异常
		if(user.isState()) {
			throw new UserException("您已经激活过注册码了");
		}
		
		//修改用户状态为true
		userDao.updateState(user.getUid(),true);
	}
	
	/**
	 * 用户登录
	 * @param form
	 * @return
	 * @throws UserException 
	 */
	public User login(User form) throws UserException {
		//使用username查询数据库得到user对象
		User user = userDao.findByUsername(form.getUsername());
		
		//比较form与user的密码是否相同
		if(user==null) {
			throw new UserException("用户不存在");
		}
		if(!user.getPassword().equals(form.getPassword()) ) {
			throw new UserException("密码错误，请重新登录");
		}
		
		//查看用户的状态
		if(!user.isState()) {
			throw new UserException("您还未激活账号,请去邮箱激活");			
		}
		
		return user;
	}
}
