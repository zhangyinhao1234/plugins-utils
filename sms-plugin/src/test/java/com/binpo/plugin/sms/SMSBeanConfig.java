package com.binpo.plugin.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.binpo.plugin.sms.base.ISMSToolService;
import com.binpo.plugin.sms.control.SMSControlCenterImpl;
import com.binpo.plugin.sms.control.i.SMSConfig;
import com.binpo.plugin.sms.control.i.SMSSpringBeanUtil;
import com.binpo.plugin.sms.impl.DHSTSMS;
import com.binpo.plugin.sms.impl.QcloudSMS;
import com.binpo.plugin.sms.impl.SYKJSMS;

/**
 * 
 * 项目自己配置
 *
 * @author zhang 2019年5月24日 下午11:04:45
 */
@Configuration
@ComponentScan(basePackages = "com.binpo.plugin.sms")
@PropertySource(value = "classpath:application.properties")
public class SMSBeanConfig {
    @Autowired
    private Environment env;

    @Bean
    public ISMSToolService sykjSMSService() {
        return new SYKJSMS();
    }

    @Bean
    public ISMSToolService qcloudSMS() {
        QcloudSMS qcloudSMS = new QcloudSMS();
        qcloudSMS.setAppid(1000000);
        qcloudSMS.setAppkey("00000000");
        qcloudSMS.setFreeSignName("X科技");
        return qcloudSMS;
    }

    @Bean
    public ISMSToolService dHSTSMS() {
        return new DHSTSMS();
    }

    @Bean
    public SMSControlCenterImpl smsControlCenter(SMSSpringBeanUtil smsSpringBeanUtil)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        SMSControlCenterImpl smsControlCenterImpl = new SMSControlCenterImpl();
        smsControlCenterImpl.setDefaultSpringids("sykjSMSService,qcloudSMS,dHSTSMS");
        smsControlCenterImpl.setSmsConfig(sMSConfig());
        smsControlCenterImpl.setApplicationContextUtil(smsSpringBeanUtil);
        return smsControlCenterImpl;
    }

    private SMSConfig sMSConfig() {
        return new SMSConfigImpl();
    }
    @Bean
    public SMSSpringBeanUtil smsSpringBeanUtil() {
        return new SMSSpringBeanUtilImpl();
    }

}
