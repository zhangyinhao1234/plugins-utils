package com.binpo.plugin.sms.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.binpo.plugin.sms.control.SMSControlCenterImpl;
import com.binpo.plugin.sms.control.i.SMSConfig;

@Configuration
@Component
public class ControllerConfig {

    

    @Value("${binpo.plugin.sms.defaultSpringIds}")
    private String defaultSpringIds;

    @Value("${binpo.plugin.sms.smsConfigClass:}")
    private String smsConfigClass;

    @Bean
    public SMSControlCenterImpl smsControlCenter()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        SMSControlCenterImpl smsControlCenterImpl = new SMSControlCenterImpl();
        smsControlCenterImpl.setDefaultSpringids(defaultSpringIds);
        smsControlCenterImpl.setSmsConfig(sMSConfig());
        return smsControlCenterImpl;
    }

    private SMSConfig sMSConfig()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (StringUtils.isBlank(smsConfigClass))
            return null;
        Class<?> forName = Class.forName(smsConfigClass);
        Object newInstance = forName.newInstance();
        return (SMSConfig) newInstance;
    }

}
