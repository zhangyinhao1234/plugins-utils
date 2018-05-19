package com.binpo.plugin.hibernate.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.binpo.plugin.hibernate.pojo.BIExample;
import com.binpo.plugin.hibernate.servce.IBIExampleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-*.xml")
public class ServiceV2Test {
	private Log logger = LogFactory.getLog(ServiceV2Test.class);
	@Autowired
	private IBIExampleService iBIExampleService ;
	
	@Test
	public void testget() {
		BIExample objById = iBIExampleService.getObjById(1L);
		logger.debug(JSON.toJSONString(objById));
	}
}
