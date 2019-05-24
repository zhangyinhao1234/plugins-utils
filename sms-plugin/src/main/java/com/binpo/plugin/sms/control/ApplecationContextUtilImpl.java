package com.binpo.plugin.sms.control;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.binpo.plugin.sms.control.i.SMSSpringBeanUtil;

public class ApplecationContextUtilImpl implements SMSSpringBeanUtil{
	@Autowired 
	private ApplicationContext context;
	@Override
	public Object getBean(String beanId) {
		Object bean = context.getBean(beanId);
		return bean;
	}

}
