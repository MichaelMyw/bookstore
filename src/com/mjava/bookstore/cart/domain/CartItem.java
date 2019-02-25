package com.mjava.bookstore.cart.domain;

import java.math.BigDecimal;

import com.mjava.bookstore.book.domain.Book;

/**
 * 购物车条目类
 * @author Mayiwei
 *
 */
public class CartItem {
	private Book book;//图书
	private int count;//数量
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	//小计
	public double getSubtotal() {
		//使用BigDecimal处理误差问题		
		BigDecimal d1 = new BigDecimal(book.getPrice()+"");
		BigDecimal d2 = new BigDecimal(count+"");
		return d1.multiply(d2).doubleValue();
	}
	@Override
	public String toString() {
		return "CartItem [book=" + book + ", count=" + count + "]";
	}
	
	
}
