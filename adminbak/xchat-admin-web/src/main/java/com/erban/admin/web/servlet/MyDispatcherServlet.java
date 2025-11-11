package com.erban.admin.web.servlet;


import com.erban.admin.web.frame.MvcContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MyDispatcherServlet extends DispatcherServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doService(HttpServletRequest request,
							 HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		MvcContext.initContext(request, response);
		super.doService(request, response);
	}

	@Override
	protected void doDispatch(HttpServletRequest request,
							  HttpServletResponse response) throws Exception {
		super.doDispatch(request, response);
	}

}
