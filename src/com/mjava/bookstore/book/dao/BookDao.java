package com.mjava.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.mjava.bookstore.book.domain.Book;
import com.mjava.bookstore.category.domain.Category;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 查询所有图书
	 * @return
	 */
	public List<Book> findAllBook(){
		String sql = "select * from book where del=false";
		try {
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 根据Bid查询图书
	 * @return
	 */
	public Book findBookByBid(String bid){
		String sql = "select * from book where bid=? and del=false";
		try {
			Map<String,Object> map= qr.query(sql, new MapHandler(),bid);
			Category category = CommonUtils.toBean(map, Category.class);
			Book book = CommonUtils.toBean(map, Book.class);
			book.setCategory(category);
			return book;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 根据类别查询图书
	 * @param cid
	 * @return
	 */
	public List<Book> findBookByCategory(String cid){
		String sql = "select * from book where cid=? and del = false";
		try {
			return qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	


	/**
	 * 获取在指定分类下的图书本书
	 * @param cid
	 * @return
	 */
	public int getCountByCid(String cid) {
		String sql="select count(*) from book where cid=? and del = false";
		try {
			Number count = (Number)qr.query(sql,new ScalarHandler(),cid);
			return count.intValue();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 添加图书
	 * @param book
	 */
	public void addBook(Book book) {
		String sql = "insert into book values(?,?,?,?,?,?,?)";
		Object[] params = {book.getBid(),book.getBname(),book.getPrice()
				,book.getAuthor(),book.getImage(),book.getCategory().getCid(),false};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/**
	 * 删除图书 把del改成true
	 * @param book
	 */
	public void deleteBook(String bid) {
		String sql = "update book set del = 1 where bid = ?";
		try {
			qr.update(sql, bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void edit(Book book) {
		String sql = "update book set bname=?,price=?,author=?,image=?,cid=? where bid = ?";
		Object[] params = {book.getBname(),book.getPrice(),book.getAuthor(),
				book.getImage(),book.getCategory().getCid(),book.getBid()};
		try {
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
