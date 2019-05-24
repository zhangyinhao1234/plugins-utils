package com.binpo.plugin.sms;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;  
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;  
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.binpo.plugin.sms.base.ISMSToolService;
import com.binpo.plugin.sms.base.SMSSendParams;
import com.binpo.plugin.sms.control.SMSControlCenter;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SMSBeanConfig.class) 
public class SMSTest {
	@Resource(name="smsControlCenter")
	private SMSControlCenter smsControlCenter;
	private String telephone="13921219802";
	private Log logger = LogFactory.getLog(this.getClass());
	
	
	@Test
	public void testSSMSConfigImplendSMS() throws IOException{
		String content="您的手机验证码是：123456,打死都不能告诉别人哦！(15分钟有效)";
		SMSSendParams params=new SMSSendParams(this.telephone, content);
		params.setType(ISMSToolService.Type.content);
		smsControlCenter.sendSMS(params);
	}
	


	@Test
    public void testSendVSMS(){
        String content="123456";
        SMSSendParams params=new SMSSendParams(this.telephone, content);
        params.setType(ISMSToolService.Type.voice);
        smsControlCenter.sendSMS(params);
    }
	
	
	
	
	

}
