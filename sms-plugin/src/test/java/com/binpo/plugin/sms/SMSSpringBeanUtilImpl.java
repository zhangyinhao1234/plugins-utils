package com.binpo.plugin.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.binpo.plugin.sms.control.i.SMSSpringBeanUtil;

public class SMSSpringBeanUtilImpl implements SMSSpringBeanUtil{
    @Autowired 
    private ApplicationContext context;
    @Override
    public Object getBean(String beanId) {
        return context.getBean(beanId);
    }

}
