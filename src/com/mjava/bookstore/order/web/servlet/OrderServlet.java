package com.mjava.bookstore.order.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mjava.bookstore.cart.domain.Cart;
import com.mjava.bookstore.cart.domain.CartItem;
import com.mjava.bookstore.order.domain.Order;
import com.mjava.bookstore.order.domain.OrderItem;
import com.mjava.bookstore.order.service.OrderException;
import com.mjava.bookstore.order.service.OrderService;
import com.mjava.bookstore.user.domain.User;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import test.PaymentUtil;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 从session中得到cart 2. 使用cart生成Order对象 3. 调用service方法完成订单 4. 保存order到request域中
		 * 5. 转发到order/desc.jsp
		 */
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		// cart转换成order对象
		// 创建order对象,并设置属性
		Order order = new Order();
		order.setOid(CommonUtils.uuid());// 设置订单编号
		order.setOrdertime(new Date());// 设置下单时间
		order.setState(1);// 设置为1表示未付款
		User user = (User) request.getSession().getAttribute("session_user");
		order.setOwner(user);// 设置订单所有者
		order.setTotal(cart.getTotal());// 设置订单小计

		// 创建订单条目集合
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		// 循环遍历Cart中所有cartItem 使用每一个CartItem对象创建OrderItem对象，并添加到集合中
		for (CartItem cartItem : cart.getCartItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setIid(CommonUtils.uuid());
			orderItem.setCount(cartItem.getCount());
			orderItem.setBook(cartItem.getBook());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setOrder(order);
			// 把订单条目添加到集合中
			orderItemList.add(orderItem);
		}
		
		// 把订单条目集合添加到订单中
		order.setOrderItemList(orderItemList);

		// 清空购物车
		cart.clear();

		// 调用service添加订单
		orderService.add(order);

		// 保存order到request域中
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}

	/**
	 * 我的订单
	 */
	public String myOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1. 从session中得到当前用户，再获取其uid 2. 调用orderService的myOrders方法得到用户所有订单List<Order> 3.
		 * 保存到request域中，并转发
		 */
		User user = (User) request.getSession().getAttribute("session_user");
		String uid = user.getUid();
		List<Order> orderList = orderService.myOrders(uid);
		request.setAttribute("orderList", orderList);
		return "f:/jsps/order/list.jsp";

	}

	/**
	 * 点击付款加载订单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取oid
		String oid = (String) request.getParameter("oid");
		// 调用Service的load方法
		Order order = orderService.loadOrder(oid);
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";

	}

	/**
	 * 确认收货
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1.获取oid 2. 调用service方法 3. 转发到msg.jsp
		 */

		String oid = request.getParameter("oid");
		try {
			orderService.confirm(oid);
			request.setAttribute("msg", "恭喜，交易成功");
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取11+1个参数
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");

		String hmac = request.getParameter("hmac");

		// 校验访问者是否为易宝
		Properties props = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
		props.load(in);

		String keyValue = props.getProperty("keyValue");
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		
		//如果校验失败，跳转到msg.jsp
		if (!bool) {
			request.setAttribute("msg", "校验失败");
			return "f:/jsps/msg.jsp";
		}
		
		//获取订单状态，确定是否要修改订单状态，以及添加积分等业务操作
		orderService.pay(r6_Order);//有可能对数据库操作，也可能不操作
		
		//判断当前回调方式，如果为点对点，需要回馈以success开头的字符串
		if(r9_BType.equals("2")) {
			response.getWriter().print("success");
		}
		
		//保存成功信息到msg.jsp
		request.setAttribute("msg", "支付成功！");
		
		
		return "f:/jsps/msg.jsp";
	}

	/**
	 * 去银行付款
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 准备13参数 ,先读取配置文件获取id和keyvalue
		 */
		Properties props = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
		props.load(in);

		String p0_Cmd = "Buy";
		String p1_MerId = props.getProperty("p1_MerId");
		String p2_Order = request.getParameter("oid");
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = props.getProperty("p8_Url");
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = request.getParameter("pd_FrpId");
		String pr_NeedResponse = "1";

		// 计算hmac
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
				p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

		// 连接易宝的网址和13+1个参数
		StringBuilder url = new StringBuilder(props.getProperty("url"));
		url.append("?p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmac=").append(hmac);

		System.out.println(url);
		response.sendRedirect(url.toString());

		return null;

	}
}
