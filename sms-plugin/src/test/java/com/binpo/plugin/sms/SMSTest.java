package com.binpo.plugin.sms;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;  
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;  
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.binpo.plugin.sms.base.SMSSendParams;
import com.binpo.plugin.sms.control.SMSControlCenter;
/**
 * 
 * 使用默认配置的短信发送
 * 应用于生产需要做的内容
 * 设置包扫描com.binpo.plugin.sms
 * 配日志文件添加，
 * binpo.plugin.sms.defaultSpringIds=短信实现类spring bean id
 * 添加短信接口的实现，代码参考DEEESMSService
 * DEEESMSService大家可以到第一信息注册个账号测试一下
 *
 * @author zhang 2018年6月24日 下午10:46:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SMSTest {
	@Resource(name="smsControlCenter")
	private SMSControlCenter smsControlCenter;
	private String telephone="13921219802";
	private Log logger = LogFactory.getLog(this.getClass());
	
	
	
	@Test
	public void testThreadSend() throws IOException{
		String content="您的手机验证码是：123456,打死都不能告诉别人哦！(15分钟有效)";
		SMSSendParams params=new SMSSendParams(this.telephone, content);
		smsControlCenter.sendSMS(params);
	}
	
	
	
	
	
	
	
	
	
	
	

}
