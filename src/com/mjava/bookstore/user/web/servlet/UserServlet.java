package com.mjava.bookstore.user.web.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mjava.bookstore.cart.domain.Cart;
import com.mjava.bookstore.user.domain.User;
import com.mjava.bookstore.user.service.UserException;
import com.mjava.bookstore.user.service.UserService;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();

	/**
	 * 注册功能
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * 1. 封装表单数据到form对象中 2. 补全uid和code 3. 输入校验 >保存错误信息，form到request域，转发到regist.jsp
		 * 4. 调用service方法完成注册 >保存错误信息，form到request域，转发到regist.jsp 5. 发邮件 6.
		 * 保存正确信息到msg.jsp
		 */

		// 封装表单数据
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		// 补全uid和code
		form.setUid(CommonUtils.uuid());
		// 用两个uuid代替授权码
		form.setCode(CommonUtils.uuid() + CommonUtils.uuid());

		/*
		 * 输入校验 
		 * 1. 创建一个map保存错误信息，key为字段名(username,password,email),value为错误信息
		 * 
		 */
		Map<String, String> errors = new HashMap<String, String>();
		// 2. 获取form中的username, password,email并校验
		String username = form.getUsername();
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空");
		} else if (username.length() < 3 || username.length() > 10) {
			errors.put("username", "用户名必须在3到10位之间");
		}

		String password = form.getPassword();
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
		} else if (password.length() < 3 || password.length() > 10) {
			errors.put("password", "密码必须在3到10位之间");
		}

		String email = form.getEmail();
		if (email == null || email.trim().isEmpty()) {
			errors.put("email", "email不能为空");
		} else if (!email.matches("\\w+@\\w+\\.\\w+")) {
			errors.put("email", "email格式错误");
		}

		// 3. 判断是否存在错误信息
		if (errors.size() > 0) {
			// 保存错误信息，form数据到request域，转发到regist.jsp
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}

		/*
		 * 调用service的regist方法
		 */

		try {
			userService.regist(form);
		} catch (UserException e) {
			// 保存异常信息，form数据到request域，转发到regist.jsp
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";

		}
		// 发邮件
		// 获取配置文件
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		String host = props.getProperty("host");// 获取主机
		String uname = props.getProperty("uname");// 获取用户名
		String pwd = props.getProperty("pwd");// 获取密码
		String from = props.getProperty("from");// 获取发件人
		String subject = props.getProperty("subject");// 获取主题
		String content = props.getProperty("content");// 获取内容
		String to = form.getEmail();// 获取收件人

		// content是{0}占位符， 后面的参数替换站位符，有几个占位符就有几个参数
		content = MessageFormat.format(content, form.getCode());// 替换占位符

		Session session = MailUtils.createSession(host, uname, pwd);// 得到session
		Mail mail = new Mail(from, to, subject, content);// 创建邮件对象

		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		/*
		 * 执行到这里，说明userService执行成功，没有抛出异常 1. 保存成功信息 2. 转发到msg.jsp
		 */
		request.setAttribute("msg", "注册成功！请马上到邮箱激活");
		return "f:/jsps/msg.jsp";

	}

	/**
	 * 激活账号
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String active(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取参数激活码
		String code = request.getParameter("code");

		// 调用service方法完成激活
		try {
			userService.active(code);

			// 保存成功信息到request域中，转发到msg.jsp
			request.setAttribute("msg", "恭喜，您激活成功了");
		} catch (UserException e) {
			// 保存异常信息到request域中，转发到msg.jsp

			request.setAttribute("msg", e.getMessage());
		}
		// 转发到msg.jsp
		return "f:/jsps/msg.jsp";

	}

	/**
	 * 登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 一键封装用户名和密码到javabean
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		// 输入校验
		Map<String, String> errors = new HashMap<String, String>();
		// 2. 获取form中的username, password,email并校验
		String username = form.getUsername();
		if (username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空");
		} else if (username.length() < 3 || username.length() > 10) {
			errors.put("username", "用户名必须在3到10位之间");
		}

		String password = form.getPassword();
		if (password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空");
		} else if (password.length() < 3 || password.length() > 10) {
			errors.put("password", "密码必须在3到10位之间");
		}
		// 3. 判断是否存在错误信息
		if (errors.size() > 0) {
			// 保存错误信息，form数据到request域，转发到regist.jsp
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}

		// 调用service方法完成登录
		try {
			User user = userService.login(form);
			
			// 保存正确信息，到session中
			request.getSession().setAttribute("session_user", user);
			
			//给用户添加一辆购物车，即在session中保存session对象
			request.getSession().setAttribute("cart", new Cart());
			
			//重定向到主页表示登录成功
			return "r:/index.jsp";

		} catch (UserException e) {
			// 保存异常信息，保存form转发到login.jsp
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
	}
	
	
	/**
	 * 用户退出 销毁session
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		return "r:/index.jsp";
	}

}
