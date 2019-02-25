package com.mjava.bookstore.user.web.servlet.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

public class AdminUserServlet extends BaseServlet{
	
	/**
	 * 后台登录
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String adminname = request.getParameter("adminname");
		String password = request.getParameter("password");
		
		
		if(adminname.equals("admin") & password.equals("123456")) {
			request.getSession().setAttribute("adminname", adminname);
			return "r:/adminjsps/admin/index.jsp";
			
		}else {
			request.setAttribute("msg","登录失败，请使用管理员账号进行登录");
			return "f:/adminjsps/login.jsp";
		}
			
	}
}
