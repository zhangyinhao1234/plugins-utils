package com.binpo.plugin.hibernate.query.page.support;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

public class QueryObject implements IQueryObject {
	protected Integer pageSize = Integer.valueOf(16);
	protected Integer currentPage = Integer.valueOf(0);
	protected String orderBy;
	protected String orderType;
	protected Map params = new HashMap();
	protected String queryString = "1=1";
	protected List<String> orderByList;
	protected List<String> orderTypeList;

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	protected void setParams(Map params) {
		this.params = params;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public Integer getCurrentPage() {
		if (this.currentPage == null) {
			this.currentPage = Integer.valueOf(-1);
		}
		return this.currentPage;
	}

	public String getOrder() {
		return this.orderType;
	}

	public String getOrderBy() {
		return this.orderBy;
	}

	public Integer getPageSize() {
		if (this.pageSize == null) {
			this.pageSize = Integer.valueOf(-1);
		}
		return this.pageSize;
	}

	/**
	 * @return the orderByList
	 */
	public List<String> getOrderByList() {
		return orderByList;
	}

	/**
	 * @param orderByList the orderByList to set
	 */
	public void setOrderByList(List<String> orderByList) {
		this.orderByList = orderByList;
	}

	/**
	 * @return the orderTypeList
	 */
	public List<String> getOrderTypeList() {
		return orderTypeList;
	}

	/**
	 * @param orderTypeList the orderTypeList to set
	 */
	public void setOrderTypeList(List<String> orderTypeList) {
		this.orderTypeList = orderTypeList;
	}

	public QueryObject() {
	}

	public QueryObject(String currentPage, ModelAndView mv, String orderBy,
			String orderType) {
		if ((currentPage != null) && (!currentPage.equals(""))) {
			setCurrentPage(Integer.valueOf(null2Int(currentPage)));
		}
		setPageSize(this.pageSize);
		if ((orderBy == null) || (orderBy.equals(""))) {
			setOrderBy("id");
			mv.addObject("orderBy", "addTime");
		} else {
			setOrderBy(orderBy);
			mv.addObject("orderBy", orderBy);
		}
		if ((orderType == null) || (orderType.equals("desc"))) {
			setOrderType("desc");
			mv.addObject("orderType", "desc");
		} else {
			setOrderType(orderType);
			mv.addObject("orderType", orderType);
		}
	}
	/**
	 * 分页查询 currentPage 为null时候则不进行分页
	 * @param currentPage
	 * @param orderBy
	 * @param orderType
	 */
	public QueryObject(String currentPage,String orderBy,String orderType) {
		if ((currentPage == null) || (currentPage.equals(""))) {
			setCurrentPage(0);
			setPageSize(-1);
		}else{
			setCurrentPage(Integer.valueOf(null2Int(currentPage)));
			setPageSize(this.pageSize);
		}
		if ((orderBy == null) || (orderBy.equals(""))) {
			setOrderBy("id");
		} else {
			setOrderBy(orderBy);
		}
		if ((orderType == null) || (orderType.equals("desc"))) {
			setOrderType("desc");
		} else {
			setOrderType(orderType);
		}
	}
	
	public QueryObject(String currentPage, ModelAndView mv, List<String> orderByList,
			List<String> orderTypeList) {
		if ((currentPage != null) && (!currentPage.equals(""))) {
			setCurrentPage(Integer.valueOf(null2Int(currentPage)));
		}
		setPageSize(this.pageSize);
		if (orderByList == null || orderByList.isEmpty()) {
			setOrderBy("id");
			mv.addObject("orderBy", "addTime");
		} else {
			setOrderByList(orderByList);
//			mv.addObject("orderBy", orderBy);
		}
		if (orderTypeList == null || orderTypeList.isEmpty()) {
			setOrderType("desc");
			mv.addObject("orderType", "desc");
		} else {
			setOrderTypeList(orderTypeList);
//			mv.addObject("orderType", orderType);
		}
	}
	
	//
	
	
	

	public PageObject getPageObj() {
		PageObject pageObj = new PageObject();
		pageObj.setCurrentPage(getCurrentPage());
		pageObj.setPageSize(getPageSize());
		if ((this.currentPage == null) || (this.currentPage.intValue() <= 0)) {
			pageObj.setCurrentPage(Integer.valueOf(1));
		}
		return pageObj;
	}

	public String getQuery() {
		customizeQuery();
		return this.queryString + orderString();
	}

	protected String orderString() {
		String orderString = "";
		StringBuffer orderStringBuff = new StringBuffer();
		if (getOrderBy() != null && !"".equals(getOrderBy()) && getOrderType() != null && !"".equals(getOrderType())) {
			orderStringBuff.append(" order by obj." + getOrderBy());
			orderStringBuff.append(" " + getOrderType());
			orderString = orderStringBuff.toString();
		}else if(getOrderByList() != null && !getOrderByList().isEmpty() && getOrderTypeList() != null && !getOrderTypeList().isEmpty()){
			for (int i = 0, length = getOrderByList().size(); i < length; i++) {
				orderStringBuff.append(",");
				orderStringBuff.append(" obj." + getOrderByList().get(i));
				orderStringBuff.append(" " + getOrderTypeList().get(i));
			}
			orderString = " order by" + orderStringBuff.substring(1);
		}
		return orderString;
	}

	public Map getParameters() {
		return this.params;
	}

	public IQueryObject addQuery(String field, SysMap para, String expression) {
		if ((field != null) && (para != null)) {
			this.queryString = (this.queryString + " and " + field + " "
					+ handleExpression(expression) + ":" + para.getKey()
					.toString());
			this.params.put(para.getKey(), para.getValue());
		}
		return this;
	}
	
	//适用于 and field in( ... )的情况
	public IQueryObject addQuery(String field, SysMap para, String expression,boolean flag) {
		
		if ((field != null) && (para != null)) {
			if(flag){
			this.queryString = (this.queryString + " and " + field + " "
					+ handleExpression(expression) + ":" + para.getKey()
					.toString());
			this.params.put(para.getKey(), para.getValue());
			}
			else{
				this.queryString = (this.queryString + " and " + field + " "
						+ handleExpression(expression) + "(:" + para.getKey()
						.toString()+")");
				this.params.put(para.getKey(), para.getValue());
			}
		}
		return this;
	}

	public IQueryObject addQuery(String field, SysMap para, String expression,
			String logic) {
		if ((field != null) && (para != null)) {
			this.queryString = (this.queryString + " " + logic + " " + field
					+ " " + handleExpression(expression) + ":" + para.getKey()
					.toString());
			this.params.put(para.getKey(), para.getValue());
		}
		return this;
	}

	public IQueryObject addQuery(String scope, Map paras) {
		if (scope != null) {
			if ((scope.trim().indexOf("and") == 0)
					|| (scope.trim().indexOf("or") == 0)) {
				this.queryString = (this.queryString + " " + scope);
			} else {
				this.queryString = (this.queryString + " and " + scope);
			}
			if ((paras != null) && (paras.size() > 0)) {
				for (Object key : paras.keySet()) {
					this.params.put(key, paras.get(key));
				}
			}
		}
		return this;
	}

	public IQueryObject addQuery(String para, Object obj, String field,
			String expression) {
		if ((field != null) && (para != null)) {
			this.queryString = (this.queryString + " and :" + para + " "
					+ expression + " " + field);
			this.params.put(para, obj);
		}
		return this;
	}

	public IQueryObject addQuery(String para, Object obj, String field,
			String expression, String logic) {
		if ((field != null) && (para != null)) {
			this.queryString = (this.queryString + " " + logic + " :" + para
					+ " " + expression + " " + field);
			this.params.put(para, obj);
		}
		return this;
	}

	private String handleExpression(String expression) {
		if (expression == null) {
			return "=";
		}
		return expression;
	}

	public void customizeQuery() {
	}
	
	private int null2Int(Object s) {
        int v = 0;
        if (s != null)
            try {
                v = Integer.parseInt(s.toString());
            } catch (Exception localException) {
            }
        return v;
    }
}
