package com.mjava.bookstore.order.service;

import java.sql.SQLException;
import java.util.List;

import com.mjava.bookstore.order.dao.OrderDao;
import com.mjava.bookstore.order.domain.Order;

import cn.itcast.jdbc.JdbcUtils;

public class OrderService {
	private OrderDao orderDao = new OrderDao();

	/**
	 * 添加订单 需要处理事务
	 * 
	 * @param order
	 */
	public void add(Order order) {
		try {
			// 开启事务
			JdbcUtils.beginTransaction();

			// 插入订单
			orderDao.addOrder(order);
			// 插入订单所有条目
			orderDao.addOrderItemList(order.getOrderItemList());
			// 上面两步绑定成一个事务
			
			// 提交事务
			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			throw new RuntimeException();
		}
	}

	/**
	 * 查找我的订单
	 * @param uid
	 * @return
	 */
	public List<Order> myOrders(String uid) {
		return orderDao.findOrdersByUid(uid);
	}
	
	
	/**
	 * 加载订单
	 * @param oid
	 * @return
	 */
	public Order loadOrder(String oid){
		return orderDao.loadOrder(oid);
	}
	
	/**
	 * 确认收货
	 * @param oid
	 * @throws OrderException
	 */
	public void confirm(String oid) throws OrderException{
		/*
		 * 检验订单状态，如果不是3，抛出异常
		 */
		
		int state = orderDao.getStateByOid(oid);
		if(state!=3) {
			throw new OrderException("订单确认失败");
		}
		
		/*
		 * 修改订单状态为4
		 * 
		 */
		orderDao.updateStated(oid, 4);
	}
	
	/**
	 * 支付方法
	 * @param oid
	 */
	public void pay(String oid) {
		//获取订单状态，如果状态为1，执行，不为1不执行
		int state = orderDao.getStateByOid(oid);
		if(state==1) {
			//修改状态为2
			orderDao.updateStated(oid, 2);
		}
	}
	
	/**
	 * 查找所有订单
	 * @return
	 */
	public List<Order> findAllOrders(){
		return orderDao.findAllOrders();
	}
	
	
	/**
	 * 查找未付款的订单
	 * @return
	 */
	public List<Order> findOrdersNotPaid(){
		return orderDao.findOrdersNotPaid();
	}
	
	/**
	 * 查找已付款的订单
	 * @return
	 */
	public List<Order> findOrdersHavePaid(){
		return orderDao.findOrdersHavePaid();
	}
	
	/**
	 * 查找未收货的订单
	 * @return
	 */
	public List<Order> findOrdersNotReceived(){
		return orderDao.findOrdersNotReceived();
	}
	
	/**
	 * 查找已完成的订单
	 * @return
	 */
	public List<Order> findOrdersFinished(){
		return orderDao.findOrdersFinished();
	}
	
	

}
