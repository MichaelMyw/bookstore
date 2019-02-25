package com.mjava.bookstore.book.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mjava.bookstore.book.domain.Book;
import com.mjava.bookstore.book.service.BookService;
import com.mjava.bookstore.category.domain.Category;
import com.mjava.bookstore.category.service.CategoryService;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	/**
	 * 查询所有图书
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAllBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Book> bookList = bookService.findAllBook();
		request.setAttribute("bookList", bookList);
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	
	/**W
	 * 加载图书
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 获取bid，调用service的方法加载图书，保存到request域中
		 * 2. 获取所有分类保存到request域中
		 */
		
		String bid = request.getParameter("bid");
		Book book = bookService.load(bid);
		request.setAttribute("book", book);
		List<Category> categoryList = categoryService.findAll();
		request.setAttribute("categoryList", categoryList);
		
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	
	
	/**
	 * 添加图书
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 调用service方法查询所有分类，保存到request域中，转发到add.jsp
		 */
		List<Category> categoryList = categoryService.findAll();
		request.setAttribute("categoryList", categoryList);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	
	/**
	 * 删除图书
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bid = request.getParameter("bid");
		bookService.delete(bid);
		return findAllBook(request, response);
	}
	
	/**
	 * 修改图书
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 封装表单数据
		 * 调用service方法完成修改
		 * 调用findAll
		 */
		Book book = CommonUtils.toBean(request.getParameterMap(), Book.class);
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		book.setCategory(category);
		bookService.edit(book);	
		return findAllBook(request, response);
	}
}
