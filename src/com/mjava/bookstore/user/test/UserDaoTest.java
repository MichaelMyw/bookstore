package com.mjava.bookstore.user.test;

import org.junit.Test;

import com.mjava.bookstore.user.dao.UserDao;
import com.mjava.bookstore.user.domain.User;
import com.mjava.bookstore.user.service.UserException;
import com.mjava.bookstore.user.service.UserService;

public class UserDaoTest {
	private UserDao userDao = new UserDao();
	private UserService userService = new UserService();
	
	@Test
	public void findByUserNameTest() {
		User user = userDao.findUserByCode("6E5BF39FD9AF4E48B02308F00D8BF7FFBDC266EA1C244937B20650E4DFE69CEF");
		System.out.println(user);
	}
	
	@Test
	public void addUserTest() throws UserException {
		User user = new User();
		user.setUid("112");
		user.setUsername("123");
		user.setPassword("abc");
		user.setEmail("121@abc.com");
		user.setCode("a");
		user.setState(true);
		
		userService.regist(user);
	}
	

	
}
