package com.mjava.bookstore.order.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mjava.bookstore.order.domain.Order;
import com.mjava.bookstore.order.service.OrderService;
import com.mjava.bookstore.user.service.UserService;

import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {

	private OrderService orderService = new OrderService();

	/**
	 * 所有订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Order> orderList = orderService.findAllOrders();		
		request.setAttribute("orderList", orderList);
		request.setAttribute("orderState", "所有订单");
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 所有已付款的订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findOrdersNotPaid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Order> orderList = orderService.findOrdersNotPaid();		
		request.setAttribute("orderList", orderList);
		request.setAttribute("orderState", "未付款订单");
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 所有未付款的订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findOrdersHavePaid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Order> orderList = orderService.findOrdersHavePaid();		
		request.setAttribute("orderList", orderList);
		request.setAttribute("orderState", "已付款订单");
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 所有未收到的订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findOrdersNotReceived(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Order> orderList = orderService.findOrdersNotReceived();		
		request.setAttribute("orderList", orderList);
		request.setAttribute("orderState", "未收货订单");
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 所有已完成订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findOrdersFinished(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Order> orderList = orderService.findOrdersFinished();		
		request.setAttribute("orderList", orderList);
		request.setAttribute("orderState", "已完成订单");
		return "f:/adminjsps/admin/order/list.jsp";
	}
}
