package com.mjava.bookstore.book.service;

import java.util.List;

import com.mjava.bookstore.book.dao.BookDao;
import com.mjava.bookstore.book.domain.Book;

public class BookService {
	private BookDao bookDao = new BookDao();
	
	/**
	 * 查询所有图书
	 * @return
	 */
	public List<Book> findAllBook(){
		return bookDao.findAllBook();
	}
	

	/**
	 * 按分类查询图书
	 * @param cid
	 * @return
	 */
	public List<Book> findBookByCategory(String cid) {
		return bookDao.findBookByCategory(cid);
	}
	
	/**
	 * 加载图书
	 * @param bid
	 * @return
	 */
	public Book load(String bid){
		return bookDao.findBookByBid(bid);
	}


	/**
	 * 添加图书
	 * @param book
	 */
	public void add(Book book) {
		bookDao.addBook(book);
		
	}
	
	/**
	 * 删除图书
	 * @param book
	 */
	public void delete(String bid) {
		bookDao.deleteBook(bid);
		
	}


	public void edit(Book book) {
		bookDao.edit(book);
		
	}
}
