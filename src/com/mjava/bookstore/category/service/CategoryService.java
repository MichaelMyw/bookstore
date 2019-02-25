package com.mjava.bookstore.category.service;

import java.util.List;

import com.mjava.bookstore.book.dao.BookDao;
import com.mjava.bookstore.category.Dao.CategoryDao;
import com.mjava.bookstore.category.domain.Category;
import com.mjava.bookstore.category.web.servlet.admin.CategoryException;

public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	private BookDao bookDao = new BookDao();

	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll() {
		// TODO Auto-generated method stub
		return categoryDao.findAll();
	}
	
	/**
	 * 添加分类
	 * @param category
	 */
	public void addCategory(Category category) {
		categoryDao.addCategory(category);
	}

	/**
	 * 删除分类
	 * @param cid
	 * @throws CategoryException 
	 */
	public void delete(String cid) throws CategoryException {
		int count = bookDao.getCountByCid(cid);
		if(count>0) {
			throw new CategoryException("该分类下还有图书，不能删除");
		}
		categoryDao.deleteCategory(cid);
	}
	
	/**
	 * 根据cid查询分类
	 * @param cid
	 * @return
	 */
	public Category findCategoryByCid(String cid) {
		return categoryDao.findCategoryByCid(cid);
	}
	
	/**
	 * 修改分类
	 */
	public void edit(Category category) {
		categoryDao.updateCategory(category);
	}
}
