package com.binpo.plugin.sms;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.binpo.plugin.sms.control.i.SMSErrorInfoSend;


public class ErrorSendInfoImpl implements SMSErrorInfoSend{
	private Log logger = LogFactory.getLog(this.getClass());
	@Override
	public void send(String errorInfo) {
		logger.debug("发送错误日志："+errorInfo);
	}

}
