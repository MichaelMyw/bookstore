package com.mjava.bookstore.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.mjava.bookstore.user.domain.User;

import cn.itcast.jdbc.TxQueryRunner;

public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 按用户名查询
	 * @param username
	 * @return
	 */
	public User findByUsername(String username) {
		try {
			String sql = "select * from tb_user where username=?";
			//我们给的是BeanHandler,他是ResultSetHandler接口的一个实现类
			//它会把rs中的结果集封装到制定类型的Javabean对象中
			return qr.query(sql, new BeanHandler<User>(User.class),username);
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按邮箱查询
	 * @param email
	 * @return
	 */
	public User findByEmail(String email) {
		try {
			String sql = "select * from tb_user where email=?";
			return qr.query(sql, new BeanHandler<User>(User.class),email);
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加用户
	 * @param user
	 */
	public void add(User user) {
		try {
			String sql = "insert into tb_user values(?,?,?,?,?,?)";
			Object[] parmas = {user.getUid(),user.getUsername(),user.getPassword(),
					user.getEmail(),user.getCode(),user.isState()};
			qr.update(sql, parmas);
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据激活码查询user
	 * @param code
	 * @return
	 */
	public User findUserByCode(String code) {
		try {
			String sql = "select * from tb_user where code=?";
			return qr.query(sql, new BeanHandler<User>(User.class),code);
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改指定用户的状态
	 * @param user
	 * 
	 */
	public void updateState(String uid, boolean state) {
		try {
			String sql = "update tb_user set state = ? where uid= ?";
			
			Object[] parmas = {state,uid};
			qr.update(sql, parmas);
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
