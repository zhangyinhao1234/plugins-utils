package com.binpo.plugin.hibernate.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.binpo.plugin.hibernate.pojo.BIExample;
import com.binpo.plugin.hibernate.query.page.support.BaseQueryObject;
import com.binpo.plugin.hibernate.query.page.support.IPageList;
import com.binpo.plugin.hibernate.query.page.support.PageQuery;
import com.binpo.plugin.hibernate.query.page.support.SysMap;
import com.binpo.plugin.hibernate.servce.IBIExampleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-*.xml")
public class ServiceV2Test {
	private Log logger = LogFactory.getLog(ServiceV2Test.class);
	@Autowired
	private IBIExampleService iBIExampleService;

	@Test
	public void testget() {
		BIExample objById = iBIExampleService.getObjById(1L);
		logger.debug(JSON.toJSONString(objById));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testQuery() {
		String hql = "select obj from BIExample obj where obj.name=:name ";
		Map params = new HashMap<>();
		params.put("name", "zhang");
		List query = this.iBIExampleService.query(hql, params, 0, -1);
		logger.debug(JSON.toJSONString(query));
		
		hql = "select obj from BIExample obj where obj.name like :name ";
		params = new HashMap<>();
		params.put("name", "%"+"z"+"%");
		query = this.iBIExampleService.query(hql, params, 0, -1);
		logger.debug(JSON.toJSONString(query));
		
		String sql="select * from binpo_bi_example where name=:name";
		params.put("name", "zhang");
		List queryForList = this.iBIExampleService.queryForList(sql, params);
		logger.debug(queryForList);
	}
	
	/**
	 * 分页插叙
	 */
	@Test
	public void testListPage() {
		BaseQueryObject qb = new BaseQueryObject("1");
		Map params = new HashMap<>();
		params.put("name", "zhang");
		qb.addQuery("obj.name=:name", params);
		qb.addQuery("obj.name", new SysMap("name","zhang"), "=");
		qb.addQuery("obj.name", new SysMap("name","%"+"zhang"+"%"), "like");
		IPageList list = this.iBIExampleService.list(qb);
		logger.debug(JSON.toJSONString(list));
	}
	
	/**
	 * 原生sql的分页
	 */
	@Test
	public void testListPage2() {
		PageQuery pageQuery = new PageQuery("select * from binpo_bi_example where 1=1 ","1");
		pageQuery.addQuery("and 1=1 ");
		pageQuery.addQuery("and name=:name", new SysMap("name", "zhang"));
		IPageList list = this.iBIExampleService.list(pageQuery);
		logger.debug(JSON.toJSONString(list));
	}
	
	
}
