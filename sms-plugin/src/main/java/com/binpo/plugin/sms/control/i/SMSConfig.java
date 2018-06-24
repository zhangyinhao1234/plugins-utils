package com.binpo.plugin.sms.control.i;

import java.util.List;
/**
 * 
 * 实现这个接口配置短信服务的参数，可以不现实这个接口，使用默认的配置
 * 默认配置为在加载的时候配置的springid
 *
 * @author zhang 2018年6月24日 下午9:50:50
 */
public interface SMSConfig {
    /**
     * 
     * 获取短信实现类的spring bean id,可以动态的从redis中获取配置的数据，使用配置的redis中的数据
     * 
     * @return
     */
    public List<String> getSpringBeanIds();

    /**
     * 
     * 短信是否开启
     * 
     * @return
     */
    public Boolean smsServerIsOpen();

    /**
     * 
     * 语音短信是否开启
     * 
     * @return
     */
    public Boolean smsVoiceServerIsOpen();
}
