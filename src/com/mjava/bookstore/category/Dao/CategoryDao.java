package com.mjava.bookstore.category.Dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.mjava.bookstore.category.domain.Category;

import cn.itcast.jdbc.TxQueryRunner;

public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll() {
		String sql = "select * from category";
		try {
			return qr.query(sql, new BeanListHandler<Category>(Category.class));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}
	
	
	/**
	 * 添加分类
	 * @param cid
	 */
	public void addCategory(Category category) {
		String sql = "insert into category (cid,cname) values(?,?)";
		try {
			qr.update(sql, category.getCid(),category.getCname());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 删除分类
	 * @param cid
	 */
	public void deleteCategory(String cid) {
		String sql = "delete from category where cid = ?";
		try {
			qr.update(sql, cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据cid查询分类
	 * @param cid
	 * @return
	 */
	public Category findCategoryByCid(String cid) {
		String sql = "select * from category where cid= ?";
		try {
			return qr.query(sql, new BeanHandler<Category>(Category.class), cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改分类
	 * @param category
	 */
	public void updateCategory(Category category) {
		String sql = "update category set cname= ? where cid=?";
		try {
			qr.update(sql,category.getCname(),category.getCid());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
