package com.mjava.bookstore.order.domain;

import java.util.Date;
import java.util.List;

import com.mjava.bookstore.user.domain.User;

/**
 * 订单类
 * @author Mayiwei
 *
 */
public class Order {
	private String oid;//订单id
	private Date ordertime;//下单时间
	private double total;//合计
	private int state;//订单状态，1为未付款，2为已付完但未发货，3为发货但未确认收货，4为确认交易成功
	private User owner;//订单所有者
	private String address;//收货地址
	
	
	private List<OrderItem> orderItemList;
	
	
	
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "Order [oid=" + oid + ", ordertime=" + ordertime + ", total=" + total + ", state=" + state + ", owner="
				+ owner + ", address=" + address + "]";
	}
	
	


}
