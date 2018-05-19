package com.binpo.plugin.hibernate.query.page.support;

import org.springframework.web.servlet.ModelAndView;

public class BaseQueryObject extends QueryObject {
	public BaseQueryObject(String currentPage, ModelAndView mv, String orderBy, String orderType) {
		super(currentPage, mv, orderBy, orderType);
	}

	/**
	 * 分页查询 currentPage 为null时候则不进行分页
	 * 
	 * @param currentPage
	 * @param orderBy
	 * @param orderType
	 */
	public BaseQueryObject(String currentPage, String orderBy, String orderType) {
		super(currentPage, orderBy, orderType);
	}

	/**
	 * 分页查询 currentPage 为null时候则不进行分页
	 * 
	 * @param currentPage
	 */
	public BaseQueryObject(String currentPage) {
		super(currentPage, null, null);
	}

	public String getQuery() {
		return super.getQuery();
	}
}