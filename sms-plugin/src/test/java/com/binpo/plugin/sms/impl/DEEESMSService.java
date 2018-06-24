package com.binpo.plugin.sms.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.binpo.plugin.sms.base.AbstractSMSServer;
import com.binpo.plugin.sms.base.SMSSendParams;
import com.binpo.plugin.sms.base.SMSUtil;
import com.binpo.plugin.sms.base.SMSUtil.SendCode;


/**
 * 第一信息的实现
 * @author zhang
 *
 */
public class DEEESMSService extends AbstractSMSServer{
	private String smsServiceName="第一信息";
	private String url="http://web.1xinxi.cn/asmx/smsservice.aspx";
	private String standbyurl="http://sms.1xinxi.cn/asmx/smsservice.aspx";
	private String account="";
	private String pswd="";
	private int sendCount=1999;
	private Log logger = LogFactory.getLog(DEEESMSService.class);

	public DEEESMSService(){
		
	}

	
	@Override
	public SendCode sendSMS(SMSSendParams params) {
		//批量短信手机最大值  示远科技 一次能给5W个手机号批量发送短信
		Set<String> spllit = SMSUtil.spllit(params.getTelephone(), ",");
		List<String> list=new ArrayList<>(spllit);
		int count = (list.size() + sendCount -1) / sendCount;
		for(int i=0;i<count;i++){
			List<String> subList = list.subList(i*sendCount, ((i+1)*sendCount)>list.size()?list.size():((i+1)*sendCount));
			String subTelephone = SMSUtil.formatSetToString(new HashSet<String>(subList));
			if(StringUtils.isNotBlank(subTelephone)&&subTelephone.length()>0){
				Map<Object,Object> httpParams = this.createParams(subTelephone, "【"+this.freeSignName+"】"+params.getContent());
				try {
					String send = SMSUtil.send(SMSUtil.createParams(httpParams), url, null);
					if(!send.startsWith(SMSUtil.SMS_IS_OK)){
						String sendBystandbyurl = sendBystandbyurl(httpParams,params.getContent());
						if(!sendBystandbyurl.startsWith(SMSUtil.SMS_IS_OK)){
							return SendCode.SMS_IS_ERROR;
						}
					}
				} catch (Exception e) {
					logger.error(e);
					String sendBystandbyurl = sendBystandbyurl(httpParams,params.getContent());
					if(!sendBystandbyurl.startsWith(SMSUtil.SMS_IS_OK)){
						return SendCode.SMS_IS_ERROR;
					}
				}	
			}
		}
		return SendCode.SMS_IS_OK;
	}

	private String sendBystandbyurl(Map<Object,Object> httpParams,String content){
		try {
			String send = SMSUtil.send(SMSUtil.createParams(httpParams), standbyurl, null);
			if(!send.startsWith("0")){
				logger.error(smsServiceName+"：异常："+send+"短信内容："+content);
				return send;
			}else{
				return SMSUtil.SMS_IS_OK;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error(e1);
			return SMSUtil.SMS_IS_ERROR;
		}
	}
	private Map<Object,Object> createParams(String telephons,String content){
		Map<Object,Object> params = new HashMap<>();
		params.put("name", this.account);
		params.put("pwd", this.pswd);
		params.put("mobile", telephons);
		params.put("type", "pt");
		params.put("content", content);
		return params;
	}

	@Override
	public String getSMSServiceName() {
		return this.smsServiceName;
	}


	@Override
	public SendCode sendVoiceSecurityCode(SMSSendParams params) {
		return SendCode.SMS_IS_ERROR;
	}

}
