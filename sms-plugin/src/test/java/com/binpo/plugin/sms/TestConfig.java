package com.binpo.plugin.sms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.binpo.plugin.sms.base.ISMSToolService;
import com.binpo.plugin.sms.impl.DEEESMSService;
import com.binpo.plugin.sms.impl.HXSMS;

@PropertySource(value="classpath:application.properties")
@ComponentScan(basePackages="com.binpo.plugin.sms")
public class TestConfig {
    /**
     * 
     * 创建一个短信发送实现类
     * 
     * @return
     */
    @Bean
    public ISMSToolService dixxSMSService() {
        return new DEEESMSService();
    }
    @Bean
    public HXSMS hXSMS() {
        return new HXSMS();
    }
}
