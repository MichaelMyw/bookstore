package com.mjava.bookstore.cart.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mjava.bookstore.book.domain.Book;
import com.mjava.bookstore.book.service.BookService;
import com.mjava.bookstore.cart.domain.Cart;
import com.mjava.bookstore.cart.domain.CartItem;

import cn.itcast.servlet.BaseServlet;

public class CartServlet extends BaseServlet{
	
	/**
	 * 在购物车中添加商品
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 得到购物车
		 * 2. 得到条目(图书和数量)
		 * 3.把条目添加到车中
		 */
		//得到购物车
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		//得到条目(先得到bid,通过查询数据库得到book)
		String bid = request.getParameter("bid");
		Book book = new BookService().load(bid);
		//得到数量
		int count = Integer.parseInt(request.getParameter("count"));
		
		CartItem cartItem = new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		
		//把条目添加到车里
		cart.add(cartItem);
		return "f:/jsps/cart/list.jsp";

	}
	

	/**
	 * 清空购物车
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}
	
	
	/**
	 * 删除购物条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		String bid = request.getParameter("bid");
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";
	}
}
