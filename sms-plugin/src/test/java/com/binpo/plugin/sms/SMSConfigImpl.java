package com.binpo.plugin.sms;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.binpo.plugin.sms.control.i.SMSConfig;
/**
 * 
 * 依赖 binpo.plugin.sms.smsConfigClass 配置，没有配置就不会加载这个文件，使用默认配置
 *
 * @author zhang 2018年6月24日 下午11:01:12
 */
public class SMSConfigImpl implements SMSConfig {
    private Log logger = LogFactory.getLog(SMSConfigImpl.class);

    @Override
    public List<String> getSpringBeanIds() {
        logger.debug("从redis中/缓存中 获取配置的短信实现spring bean id。。。。。。");
        return Stream.of("dixxSMSService").collect(Collectors.toList());
    }

    @Override
    public Boolean smsServerIsOpen() {
        logger.debug("从redis中/缓存中 获取短信是否开启。。。。。。");
        return true;
    }

    @Override
    public Boolean smsVoiceServerIsOpen() {
        logger.debug("从redis中/缓存中 获取语音短信是否开启。。。。。。");
        return true;
    }

}
