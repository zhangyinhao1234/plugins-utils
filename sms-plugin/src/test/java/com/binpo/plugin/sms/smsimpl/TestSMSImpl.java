package com.binpo.plugin.sms.smsimpl;

import com.binpo.plugin.sms.base.AbstractSMSServer;
import com.binpo.plugin.sms.base.SMSSendParams;
import com.binpo.plugin.sms.base.SMSUtil.SendCode;

/**
 * 
 * 接入新的短信供应商添加新的实现类
 *
 * @author zhang 2019年5月24日 下午10:04:49
 */
public class TestSMSImpl extends AbstractSMSServer{

	@Override
	public SendCode sendVoiceSecurityCode(SMSSendParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSMSServiceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SendCode sendSMS(SMSSendParams params) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public String getFreeSignName() {
        // TODO Auto-generated method stub
        return null;
    }

	

}
