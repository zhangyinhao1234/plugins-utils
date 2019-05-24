package com.binpo.plugin.sms;


import java.util.List;


import com.binpo.plugin.sms.control.i.SMSConfig;

public class SMSConfigImpl implements SMSConfig{

	@Override
	public List<String> getSpringBeanIds() {
		//可以从redis动态获取 
		return null;
	}

	@Override
	public Boolean smsServerIsOpen() {
	    //可以从Redis动态获取
		return true;
	}

	@Override
	public Boolean smsVoiceServerIsOpen() {
	    //可以从Redis动态获取
		return true;
	}

}
