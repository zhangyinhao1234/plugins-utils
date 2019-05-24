package com.binpo.plugin.sms.base;

import com.binpo.plugin.sms.base.SMSUtil.SendCode;

/**
 * 
 * 短信服务
 * @author zhang
 *
 */
public interface ISMSToolService {
    /**
     * 
     * 短信类型：文本/语音
     *
     * @author zhang 2019年5月24日 下午10:11:45
     */
	public interface Type{
		public String content="content";
		
		public String voice="voice";
	}
	/**
	 * 发送短信
	 * @param telephone
	 * @param content
	 * @param 短信模板编号
	 * @param 如果使用短信模板编号，那么需要使用key-value参数
	 * @author zhang
	 * @date 2016年2月27日下午10:12:44
	 * @version 0.01
	 * @return String
	 */
	public SendCode sendSMS(SMSSendParams params);
	/**
	 * 
	 * 发送语音验证码
	 * 
	 * @param params
	 * @return
	 */
	public SendCode sendVoiceSecurityCode(SMSSendParams params);
	
	/**
	 * 获取短信服务商名称
	 * @return
	 * @author zhang
	 * @date 2016年3月5日下午11:32:56
	 * @version 0.01
	 * @return String
	 */
	public String getSMSServiceName();
	

}
