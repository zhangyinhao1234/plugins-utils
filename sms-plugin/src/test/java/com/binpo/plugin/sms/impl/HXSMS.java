package com.binpo.plugin.sms.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.binpo.plugin.sms.base.AbstractSMSServer;
import com.binpo.plugin.sms.base.SMSSendParams;
import com.binpo.plugin.sms.base.SMSUtil;
import com.binpo.plugin.sms.base.SMSUtil.SendCode;


public class HXSMS extends AbstractSMSServer{
	//华兴短信平台
	private String smsServiceName="华兴";
	private String hx_url="http://www.stongnet.com/sdkhttp/sendsms.aspx";
	private String hx_reg="";
	private String hx_pwd="";
	private String hx_sourceadd="";
	private int sendCount=1999;
	private Log logger = LogFactory.getLog(HXSMS.class);
	@Override
	public String getSMSServiceName() {
		return smsServiceName;
	}

	@Override
	public SendCode sendSMS(SMSSendParams params) {
		Set<String> spllit = SMSUtil.spllit(params.getTelephone(), ",");
		List<String> list=new ArrayList<>(spllit);
		int count = (list.size() + sendCount -1) / sendCount;
		String encode;
		try {
			encode = URLEncoder.encode(params.getContent(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			return SendCode.SMS_IS_ERROR;
		}
		for(int i=0;i<count;i++){
			List<String> subList = list.subList(i*sendCount, ((i+1)*sendCount)>list.size()?list.size():((i+1)*sendCount));
			String subTelephone = SMSUtil.formatSetToString(new HashSet<String>(subList));
			if(StringUtils.isNotBlank(subTelephone)&&subTelephone.length()>0){
				String strSchSmsParam = "reg=" + hx_reg + "&pwd=" + hx_pwd + "&sourceadd=" + hx_sourceadd + "&tim=" + "" + "&phone=" + subTelephone + "&content=" + encode;
				String result = SMSUtil.postSend(hx_url, strSchSmsParam);
				if(result==null||!result.startsWith("result=0")){
					logger.error(smsServiceName+"：异常："+result+"短信内容："+params.getContent());
					return SendCode.SMS_IS_ERROR;
				}
			}
		}
		return SendCode.SMS_IS_OK;
	}


	@Override
	public SendCode sendVoiceSecurityCode(SMSSendParams params) {
		return SendCode.SMS_IS_ERROR;
	}

}
