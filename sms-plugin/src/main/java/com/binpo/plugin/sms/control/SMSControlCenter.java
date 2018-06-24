package com.binpo.plugin.sms.control;

import com.binpo.plugin.sms.base.SMSSendParams;



public interface SMSControlCenter {
	/**
	 * 发送短信
	 * @param telephone 手机号
	 * @param content 内容
	 * @param smsTemplateCode 缺省值 null 短信模板的id，特殊的短信平需要使用模板id发送
	 * @param params 模板id的 key-value
	 * @author zhang
	 * @date 2016年3月10日上午10:47:55
	 * @version 0.01
	 * @return void
	 */
	public void sendSMS(SMSSendParams params);
	/**
	 * 语音验证码接口
	 * @param params
	 * @author zhang
	 * @date 2016年4月4日下午9:26:06
	 * @version 0.01
	 * @return void
	 */
	
	public boolean closeSMSServer();
	
	public boolean openSMSServer();

}
