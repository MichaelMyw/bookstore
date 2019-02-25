package com.mjava.bookstore.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mjava.bookstore.category.domain.Category;
import com.mjava.bookstore.category.service.CategoryService;

import cn.itcast.servlet.BaseServlet;

public class CategoryServlet extends BaseServlet{
	private CategoryService categoryService = new CategoryService();
	
	/**
	 * 查询所有类别
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Category> categoryList = categoryService.findAll();
		request.setAttribute("categoryList", categoryList);
		
		return "f:/jsps/left.jsp";
	}
}
