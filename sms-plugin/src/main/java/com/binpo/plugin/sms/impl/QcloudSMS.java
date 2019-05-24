package com.binpo.plugin.sms.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.binpo.plugin.sms.base.AbstractSMSServer;
import com.binpo.plugin.sms.base.SMSSendParams;
import com.binpo.plugin.sms.base.SMSUtil;
import com.binpo.plugin.sms.base.SMSUtil.SendCode;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.SmsVoiceVerifyCodeSender;
import com.github.qcloudsms.SmsVoiceVerifyCodeSenderResult;

/**
 * 
 * 腾讯云短信实现
 *
 */
public class QcloudSMS extends AbstractSMSServer {
    private String smsServiceName = "腾讯云";
    private int appid = 10000000;
    private String appkey = "000000000";
    private int sendCount = 200;

    private Log logger = LogFactory.getLog(SYKJSMS.class);

    @Override
    public SendCode sendVoiceSecurityCode(SMSSendParams params) {
        // 语音验证码发送
        try {
            SmsVoiceVerifyCodeSender smsSingleVoiceSender = new SmsVoiceVerifyCodeSender(appid, appkey);
            SmsVoiceVerifyCodeSenderResult send = smsSingleVoiceSender.send("86", params.getTelephone(),
                    params.getContent(), 2, "");
            logger.debug(this.smsServiceName + ":短信发送结果：" + send.toString());
            if (send.result != 0) {
                logger.debug(this.smsServiceName + "发送异常，原因：" + send.errMsg);
                return SendCode.SMS_IS_ERROR;
            }
        } catch (Exception e) {
            logger.error(this.smsServiceName + "短信服务异常", e);
            return SendCode.SMS_IS_ERROR;
        }
        return SendCode.SMS_IS_OK;
    }

    @Override
    public String getSMSServiceName() {
        return this.smsServiceName;
    }

    @Override
    public SendCode sendSMS(SMSSendParams params) {
        Set<String> spllit = SMSUtil.spllit(params.getTelephone(), ",");
        if (spllit.size() <= 1) {
            return smsSingleSender(params);
        } else {
            return smsMultiSender(params);
        }
    }

    private SendCode smsSingleSender(SMSSendParams params) {
        try {
            SmsSingleSender singleSender = new SmsSingleSender(appid, appkey);
            // 普通单发
            SmsSingleSenderResult singleSenderResult = singleSender.send(0, "86", params.getTelephone(),
                    params.getContent(), "", "");
            logger.debug(this.smsServiceName + ":短信发送结果：" + singleSenderResult.toString());
            if (singleSenderResult.result != 0) {
                logger.debug(this.smsServiceName + "发送异常，原因：" + singleSenderResult.errMsg);
                return SendCode.SMS_IS_ERROR;
            }
        } catch (Exception e) {
            logger.error(this.smsServiceName + "短信服务异常", e);
            return SendCode.SMS_IS_ERROR;
        }
        return SendCode.SMS_IS_OK;
    }

    private SendCode smsMultiSender(SMSSendParams params) {
        Set<String> spllit = SMSUtil.spllit(params.getTelephone(), ",");
        List<String> list = new ArrayList<>(spllit);
        int count = (list.size() + sendCount - 1) / sendCount;
        for (int i = 0; i < count; i++) {
            List<String> subList = list.subList(i * sendCount,
                    ((i + 1) * sendCount) > list.size() ? list.size() : ((i + 1) * sendCount));
            try {
                ArrayList<String> telList = new ArrayList<String>(subList.size());
                for (String tel : subList) {
                    telList.add(tel);
                }
                // 初始化群发
                SmsMultiSender multiSender = new SmsMultiSender(appid, appkey);
                // 普通群发
                SmsMultiSenderResult multiSenderResult = multiSender.send(0, "86", telList,
                        params.getContent(), "", "");
                logger.debug(this.smsServiceName + ":群发结果：" + multiSenderResult.toString());
                if (multiSenderResult.result != 0) {
                    logger.debug(this.smsServiceName + "发送异常，原因：" + multiSenderResult.errMsg);
                    return SendCode.SMS_IS_ERROR;
                }
            } catch (Exception e) {
                logger.error(this.smsServiceName + "短信服务异常", e);
                return SendCode.SMS_IS_ERROR;
            }
        }
        return SendCode.SMS_IS_OK;
    }

    private String signName = "";

    @Override
    public String getFreeSignName() {
        return signName;
    }

    public void setFreeSignName(String signName) {
        this.signName = signName;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

}
