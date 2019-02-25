package com.mjava.bookstore.category.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mjava.bookstore.category.domain.Category;
import com.mjava.bookstore.category.service.CategoryService;
import com.mjava.bookstore.user.domain.User;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	
	/**
	 * 
	 * 后台查询所有分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 调用service方法得到所有分类
		 * 保存到request，转发到list.jsp
		 */
		
		List<Category> categoryList = categoryService.findAll();
		request.setAttribute("categoryList", categoryList);
		return "f:/adminjsps/admin/category/list.jsp";

	}
	
	/**
	 * 添加分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 封装表单数据
		 * 2. 补全cid
		 * 3. 调用service的方法
		 * 4. 调用findAll(),完成查询和转发
		 */
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.addCategory(category);
		return findAll(request, response);
	}
	
	/**
	 * 删除分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 获取cid
		 * 2. 调用service的方法传入cid
		 * 	如果出现一次，保存异常信息转发到msg.jsp
		 * 3. 调用findAll(),完成查询和转发
		 */
		String cid = request.getParameter("cid");
		try {
			categoryService.delete(cid);
			return findAll(request, response);

		}catch (CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/admin/msg.jsp";
		}
		
	}
	
	/**
	 * 加载分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 获取cid
		 * 2. 通过cid调用service方法，得到category对象
		 * 保存到request域中 ，转发到mod.jsp
		 */
		String cid = request.getParameter("cid");
		Category category = categoryService.findCategoryByCid(cid);
		request.setAttribute("category", category);
		return "f:/adminjsps/admin/category/mod.jsp";
	}
	
	/**
	 * 修改分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 封装表单数据
		 * 2. 调用service方法完成修改工作
		 * 3. 调用findAll
		 */
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		categoryService.edit(category);
		return findAll(request, response);
	}
	
	
}
