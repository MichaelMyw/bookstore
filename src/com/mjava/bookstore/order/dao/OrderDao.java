package com.mjava.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.mjava.bookstore.book.domain.Book;
import com.mjava.bookstore.order.domain.Order;
import com.mjava.bookstore.order.domain.OrderItem;
import com.mjava.bookstore.user.domain.User;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 添加订单
	 */
	public void addOrder(Order order) {
		try {
			String sql = "insert into orders values(?,?,?,?,?,?)";

			/*
			 * 处理util的date转换成sql的timestamp
			 */
			Timestamp timeStamp = new Timestamp(order.getOrdertime().getTime());

			Object[] params = { order.getOid(), timeStamp, order.getTotal(), order.getState(),
					order.getOwner().getUid(), order.getAddress() };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 插入订单条目
	 * @param listITemList
	 */
	public void addOrderItemList(List<OrderItem> orderItemList) {
		/*
		 * QueryRunner类  的batch(String sql,Object[][] parmas)
		 * 其中parmas是一个二维数组，可以理解为多个一维数组
		 * 每个一维数组都与sql在一起执行一次，多个一维数组就执行多次
		 * 这样就是批处理
		 */		
		try {
			String sql = "insert into orderitem values (?,?,?,?,?)";
			Object[][] params= new Object[orderItemList.size()][];
			/*
			 * 循环遍历orderItemList,使用每个orderITem对象为params中每个一维数组赋值
			 */
			for (int i = 0; i<orderItemList.size();i++) {
				OrderItem orderItem = orderItemList.get(i);
				params[i] = new Object[] {
					orderItem.getIid(),orderItem.getCount(),
					orderItem.getSubtotal(),orderItem.getOrder().getOid(),
					orderItem.getBook().getBid()					
				};
			}
			qr.batch(sql, params);//执行批处理
		  }catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据uid查询订单
	 * 
	 * @param uid
	 * @return
	 */
	public List<Order> findOrdersByUid(String uid){
		/*
		 * 1. 根据uid查询所有List<Order>
		 * 2. 循环遍历所有的order，加载各自的orderItem
		 */
		
		//得到当前用户的所有订单
		String sql ="select * from orders where uid = ?";
		try {
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class), uid);
			
			//循环遍历每个order为其加载自己所有的订单条目
			for (Order order : orderList) {
				loadOrderItem(order);
			}
			
			//返回订单列表
			return orderList;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * 
	 * 加载订单
	 * 
	 * @param oid
	 * @return
	 */
	public Order loadOrder(String oid){
		String sql = "select * from orders where oid = ?";
		try {
			//得到当前订单号的订单
			Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);
					
			//循环遍历这个order为其加载自己所有的订单条目
			loadOrderItem(order);

			//返回订单
			return order;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}
	
	
	/**
	 * 根据oid查找订单状态
	 * @param oid
	 * @return
	 */
	public int getStateByOid(String oid) {
		String sql = "select state from orders where oid=?";
		try {
			Number number = (Number) qr.query(sql, new ScalarHandler(), oid);
			return number.intValue();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 修改订单状态
	 * @param oid
	 * @return
	 */
	public void updateStated(String oid,int state) {
		String sql = "update orders set state = ? where oid=?";
		try {
			qr.update(sql, state,oid);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查找所有订单(包括username)
	 * @return
	 */
	public List<Order> findAllOrders(){
		String sql = "select * from orders,tb_user where orders.uid = tb_user.uid";
		try {
			//由于不是一个javabean，只能使用mapListHandler
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());

			//把map对象转化成OrderList
			List<Order> orderList = new ArrayList<Order>();
				for (Map<String,Object> map : mapList) {
					Order order = CommonUtils.toBean(map, Order.class);
					User user = CommonUtils.toBean(map, User.class);
					order.setOwner(user);
					orderList.add(order);
				}
	
			//循环遍历每个order为其加载自己所有的订单条目
			for (Order order : orderList) {
				loadOrderItem(order);
			}
			
			//返回订单列表
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 查找未付款订单(包括username)
	 * @return
	 */
	public List<Order> findOrdersNotPaid(){
		String sql = "select * from orders,tb_user where orders.uid = tb_user.uid and orders.state = 1";
		try {
			//由于不是一个javabean，只能使用mapListHandler
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());

			//把map对象转化成OrderList
			List<Order> orderList = new ArrayList<Order>();
				for (Map<String,Object> map : mapList) {
					Order order = CommonUtils.toBean(map, Order.class);
					User user = CommonUtils.toBean(map, User.class);
					order.setOwner(user);
					orderList.add(order);
				}
	
			//循环遍历每个order为其加载自己所有的订单条目
			for (Order order : orderList) {
				loadOrderItem(order);
			}
			
			//返回订单列表
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查找已付款订单(包括username)
	 * @return
	 */
	public List<Order> findOrdersHavePaid(){
		String sql = "select * from orders,tb_user where orders.uid = tb_user.uid and orders.state = 2";
		try {
			//由于不是一个javabean，只能使用mapListHandler
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());

			//把map对象转化成OrderList
			List<Order> orderList = new ArrayList<Order>();
				for (Map<String,Object> map : mapList) {
					Order order = CommonUtils.toBean(map, Order.class);
					User user = CommonUtils.toBean(map, User.class);
					order.setOwner(user);
					orderList.add(order);
				}
	
			//循环遍历每个order为其加载自己所有的订单条目
			for (Order order : orderList) {
				loadOrderItem(order);
			}
			
			//返回订单列表
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查找未收到已付款订单(包括username)
	 * @return
	 */
	public List<Order> findOrdersNotReceived(){
		String sql = "select * from orders,tb_user where orders.uid = tb_user.uid and orders.state = 3";
		try {
			//由于不是一个javabean，只能使用mapListHandler
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());

			//把map对象转化成OrderList
			List<Order> orderList = new ArrayList<Order>();
				for (Map<String,Object> map : mapList) {
					Order order = CommonUtils.toBean(map, Order.class);
					User user = CommonUtils.toBean(map, User.class);
					order.setOwner(user);
					orderList.add(order);
				}
	
			//循环遍历每个order为其加载自己所有的订单条目
			for (Order order : orderList) {
				loadOrderItem(order);
			}
			
			//返回订单列表
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查找已完成订单(包括username)
	 * @return
	 */
	public List<Order> findOrdersFinished(){
		String sql = "select * from orders,tb_user where orders.uid = tb_user.uid and orders.state = 4";
		try {
			//由于不是一个javabean，只能使用mapListHandler
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());

			//把map对象转化成OrderList
			List<Order> orderList = new ArrayList<Order>();
				for (Map<String,Object> map : mapList) {
					Order order = CommonUtils.toBean(map, Order.class);
					User user = CommonUtils.toBean(map, User.class);
					order.setOwner(user);
					orderList.add(order);
				}
	
			//循环遍历每个order为其加载自己所有的订单条目
			for (Order order : orderList) {
				loadOrderItem(order);
			}
			
			//返回订单列表
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/**
	 * 加载订单条目
	 * @param order
	 */
	private void loadOrderItem(Order order) {
		//因为orderitem表只有书的bid没有书名，所以我们要做多表查询
		String sql = "select * from orderitem i, book b where i.bid=b.bid and oid=?";
		
		try {
			//因为一行结果集不是一个javabean所以不能用beanlisthandler 要用maplisthandler
			List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
			//List里面是个map{bid=xxxx,bname=xxx....}
			//mapList是多个map，每个map对应一行结果集
			//我们需要使用一个map生成两个对象:OrderItem,Book然后建立两者的关系(把book设置给Order)
			
			//循环遍历每个Map，使用map生成两个对象，然后建立关系
			List<OrderItem> orderItemList = toOrderItemList(mapList);
			//把orderItem保存到order中
			order.setOrderItemList(orderItemList);
		
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * 把maplist每个map转换成两个对象，并建立关系
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		//循环遍历maplist 生成orderItemList
		for (Map<String, Object> map : mapList) {
			OrderItem item = toOrderItem(map);
			orderItemList.add(item);
		}
		return orderItemList;
	}

	/**
	 * 把一个map转换成一个OrderItem对象
	 * @param map
	 * @return
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

}
