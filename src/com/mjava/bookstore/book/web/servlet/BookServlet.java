package com.mjava.bookstore.book.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mjava.bookstore.book.domain.Book;
import com.mjava.bookstore.book.service.BookService;

import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet{
	private BookService bookService = new BookService();
	
	
	/**
	 * 查询所有图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAllBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Book> bookList = bookService.findAllBook();
		request.setAttribute("bookList", bookList);
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 按分类查询图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findBookByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求参数cid
		String cid = request.getParameter("cid");
		
		List<Book> bookList = bookService.findBookByCategory(cid);
		request.setAttribute("bookList", bookList);
		return "f:/jsps/book/list.jsp";
	}
	
	
	/**
	 * 加载
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求参数bid
		String bid = request.getParameter("bid");
		//调用service的load方法
		Book book = bookService.load(bid);
		//保存数据并转发到desc.jsp
		request.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}

}
