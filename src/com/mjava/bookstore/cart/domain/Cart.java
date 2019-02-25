package com.mjava.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车类
 * @author Mayiwei
 *
 */
public class Cart {
	//购物车需要排序，使用LinkedHashMap，查找也比较方便
	private Map<String,CartItem> map = new LinkedHashMap<String, CartItem>();
	
	//总计,所有条目的和
	public double getTotal() {
		BigDecimal total = new BigDecimal("0");
		
		for (CartItem cartItem : map.values()) {
			BigDecimal subtotal = new BigDecimal(cartItem.getSubtotal()+"");
			total = total.add(subtotal);
			
		}
		
		return total.doubleValue();
	}
	
	
	//添加商品条目
	public void add(CartItem cartItem) {
		//如果存在原条目的商品
		if(map.containsKey(cartItem.getBook().getBid())){
			//返回老条目
			CartItem _cartItem = map.get(cartItem.getBook().getBid());
			//设置新条目的数量等于老条目的数量加上新增的
			cartItem.setCount(cartItem.getCount()+_cartItem.getCount());
		}else {
			//如果条目不存在，则添加这个条目
			map.put(cartItem.getBook().getBid(), cartItem);
		}
	}
	
	//清空购物车，所有条目
	public void clear() {
		map.clear();
	}
	
	//删除商品
	public void delete(String bid) {
		map.remove(bid);
	}
	
	//得到商品条目
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	
	
	
}
