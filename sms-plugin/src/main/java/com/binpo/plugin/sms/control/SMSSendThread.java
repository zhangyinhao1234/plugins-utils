package com.binpo.plugin.sms.control;

import com.binpo.plugin.sms.base.SMSSendParams;

public class SMSSendThread implements Runnable{
	private SMSControlCenter smsControlCenter;
	private SMSSendParams params;
	public SMSSendThread(SMSControlCenter smsControlCenter,SMSSendParams params){
		this.smsControlCenter=smsControlCenter;
		this.params= params;
	}
	@Override
	public void run() {
		smsControlCenter.sendSMS(params);
	}

}
