package com.mjava.bookstore.user.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mjava.bookstore.user.domain.User;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(
		urlPatterns = { 
				"/jsps/cart/*", 
				"/jsps/order/*"
		}, 
		servletNames = { 
				"CartServlet", 
				"OrderServlet"
		})
public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		/*
		 * 1. 从session中获取用户信息，如果session中存在放行，否则保存错误信息转发到login.jsp
		 * 
		 */
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		

		User user = (User)httpServletRequest.getSession().getAttribute("session_user");
		if(user != null) {
			chain.doFilter(request, response);

		}else {
			httpServletRequest.setAttribute("msg", "你还没有登录");
			httpServletRequest.getRequestDispatcher("jsps/user/login.jsp").forward(httpServletRequest, httpServletResponse);
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
