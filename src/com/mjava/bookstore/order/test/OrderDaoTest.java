package com.mjava.bookstore.order.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mjava.bookstore.order.dao.OrderDao;
import com.mjava.bookstore.order.domain.Order;

public class OrderDaoTest {
	
	private OrderDao orderDao = new OrderDao();
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void findOrderByIdTest() {
		List<Order> orderList =  orderDao.findOrdersByUid("8C8882FA300F4CF3931B61BE764D4DE0");
		for (Order order : orderList) {
			System.out.println(order.getOrderItemList());
		}
	}
	
	

}

