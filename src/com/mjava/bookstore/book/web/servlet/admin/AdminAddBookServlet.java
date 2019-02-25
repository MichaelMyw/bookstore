package com.mjava.bookstore.book.web.servlet.admin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mjava.bookstore.book.domain.Book;
import com.mjava.bookstore.book.service.BookService;
import com.mjava.bookstore.category.domain.Category;
import com.mjava.bookstore.category.service.CategoryService;

import cn.itcast.commons.CommonUtils;

/**
 * Servlet implementation class AdminAddBookServlet
 */
public class AdminAddBookServlet extends HttpServlet {
	
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 把表单数据封装到Book对象中 
		 * 	上传三步
		 */
		//创建工厂
		DiskFileItemFactory factory = new DiskFileItemFactory(30 * 1024, new File("F:/f/temp"));
		//得到解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		//设置单个文件大小为30kb
		sfu.setFileSizeMax(30*1024);
		//使用解析器得到request对象,得到List<FileItem>对象
		try {
			List<FileItem> fileItemList = sfu.parseRequest(request);
			//把fileItemList的数据封装到Book对象中
			/*
			 * 把所有普通表单字段数据封装到map中，再把map中的数据封装到Book对象中 
			 */
			Map<String,String> map = new HashMap<String,String>();
			for (FileItem fileItem : fileItemList) {
				if(fileItem.isFormField()) {
					map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
				}
			}
			
			
			Book book = CommonUtils.toBean(map, Book.class);
			book.setBid(CommonUtils.uuid());
			
			
			//需要把map中的cid封装到category中，再把category赋给book
			Category category = CommonUtils.toBean(map, Category.class);
			
			book.setCategory(category);
			
			
			
			/*
			 * 保存上传的文件
			 * 	保存的目录
			 * 	保存的文件名称
			 */
			
			//得到保存的目录
			String savePath = this.getServletContext().getRealPath("/book_img");
			//得到文件名称：给原来的文件名称添加uuid前缀，避免文件名冲突
			String filename = CommonUtils.uuid() + "_" + fileItemList.get(1).getName();
			//使用目录和文件名称创建目标文件
			File deskFile = new File(savePath, filename);
			
			/*
			 * 校验文件的拓展名，只允许jpg
			 */
			if(!filename.toLowerCase().endsWith("jpg")) {
				request.setAttribute("msg", "您上传的图片必须要是jpg格式");
				List<Category> categoryList = categoryService.findAll();
				request.setAttribute("categoryList", categoryList);
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);	
				return;
			}
			
			
			
			
			//保存上传文件到目标文件位置
			fileItemList.get(1).write(deskFile);
			
			/*
			 * 设置book对象的image，即把图片的路径设置给book的image
			 */
			book.setImage("book_img/" + filename);
			
			/*
			 * 使用bookservice完成保存
			 */
			bookService.add(book);
			
			
			/*
			 * 校验图片的尺寸
			 * 
			 */
			Image image = new ImageIcon(deskFile.getAbsolutePath()).getImage();
			if(image.getWidth(null)>200||image.getHeight(null)>200) {
				//删除这个文件
				deskFile.delete();
				request.setAttribute("msg", "您上传的图片尺寸超出了200*200");
				List<Category> categoryList = categoryService.findAll();
				request.setAttribute("categoryList", categoryList);
			request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
				return;
			}
			/*
			 * 返回图书列表
			 * 
			 */
			
			request.getRequestDispatcher("/admin/AdminBookServlet?method=findAllBook").forward(request, response);
			
		
		} catch (Exception e) {
			if(e instanceof FileUploadBase.FileSizeLimitExceededException) {
				request.setAttribute("msg", "您上传的文件超过了30kb");
				List<Category> categoryList = categoryService.findAll();
				request.setAttribute("categoryList", categoryList);
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
			}
		}
	}

}
