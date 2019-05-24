package com.binpo.plugin.sms.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.binpo.plugin.sms.base.AbstractSMSServer;
import com.binpo.plugin.sms.base.SMSSendParams;
import com.binpo.plugin.sms.base.SMSUtil;
import com.binpo.plugin.sms.base.SMSUtil.SendCode;

/**
 * 
 * 大汉三通短信实现
 *
 * @author zhang 2019年5月24日 下午10:34:22
 */
public class DHSTSMS extends AbstractSMSServer {
    private String smsServiceName = "大汉三通";
    private final String batchurl = "http://www.dh3t.com/json/sms/BatchSubmit";
    /**
     * 备用地址
     */
    private final String barchurl_bak = "http://www.dh3t.com/json/sms/BatchSubmit";

    private String account = "xxxxxx";
    private String pswd = "4a687025cb2a7d9f79991ee7811afdaa";
    // 语音验证码请求地址
    private String voiceUrl = "http://voice.3tong.net/json/voiceSms/SubmitVoc";
    private String voiceAccount = "xxxxx";
    private String voicePswd = "4a687025cb2a7d9f79991ee7811afdaa";
    private int sendCount = 500;

    private Log logger = LogFactory.getLog(DHSTSMS.class);

    @Override
    public SendCode sendVoiceSecurityCode(SMSSendParams params) {
        Map<String, Object> createVoicParams = this.createVoicParams(params.getTelephone(),
                params.getContent());
        String jsonString = JSON.toJSONString(createVoicParams);
        try {
            String send = SMSUtil.send(voiceUrl, jsonString);
            send = new String(send.getBytes("ISO-8859-1"));
            if (voicSuccess(send)) {
                logger.debug("发送返回结果：" + send);
                return SendCode.SMS_IS_OK;
            } else {
                logger.debug("发送异常：返回结果：" + send);
                return SendCode.SMS_IS_ERROR;
            }
        } catch (Exception e) {
            logger.error(e);
            return SendCode.SMS_IS_ERROR;
        }
    }

    private boolean voicSuccess(String result) {
        Map<String, String> parseObject = JSON.parseObject(result, Map.class);
        String string = parseObject.get("result");
        if ("DH:0000".equals(string)) {
            return true;
        }
        return false;
    }

    private Map<String, Object> createVoicParams(String telephons, String content) {
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
        params.put("account", this.voiceAccount);
        params.put("password", this.voicePswd);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("msgid", UUID.randomUUID().toString());
        data.put("callee", telephons);
        data.put("text", content);
        data.put("calltype", 1);
        data.put("playmode", 0);
        datalist.add(data);
        params.put("data", datalist);
        return params;
    }

    private SendCode sendContentSMS(SMSSendParams params) {
        Set<String> spllit = SMSUtil.spllit(params.getTelephone(), ",");
        List<String> list = new ArrayList<>(spllit);
        int count = (list.size() + sendCount - 1) / sendCount;
        for (int i = 0; i < count; i++) {
            List<String> subList = list.subList(i * sendCount,
                    ((i + 1) * sendCount) > list.size() ? list.size() : ((i + 1) * sendCount));
            String subTelephone = SMSUtil.formatSetToString(new HashSet<String>(subList));
            if (StringUtils.isNotBlank(subTelephone) && subTelephone.length() > 0) {
                Map<String, Object> httpParams = this.createParams(subTelephone, params.getContent());
                try {
                    String jsonString = JSON.toJSONString(httpParams);
                    logger.debug("发送短信内容：" + jsonString);
                    String send = SMSUtil.send(batchurl, jsonString);
                    send = new String(send.getBytes("ISO-8859-1"));
                    boolean result = getResult(send);
                    if (!result) {
                        logger.error(smsServiceName + "：异常：" + send + "短信内容：" + params.getContent());
                        return SendCode.SMS_IS_ERROR;
                    } else {
                        // if(send.equals("error")){
                        // return sendofBack(httpParams,params);
                        // }
                        logger.error(send);
                        return SendCode.SMS_IS_OK;
                    }
                } catch (Exception e) {
                    logger.error(e);
                    return SendCode.SMS_IS_ERROR;
                    // return sendofBack(httpParams,params);
                }
            }
        }
        return SendCode.SMS_IS_OK;
    }

    private boolean getResult(String result) {
        Map<String, String> parseObject = JSON.parseObject(result, Map.class);
        String string = parseObject.get("result");
        if ("0".equals(string))
            return true;
        return false;
    }

    @Override
    public String getSMSServiceName() {
        return smsServiceName;
    }

    @Override
    public SendCode sendSMS(SMSSendParams params) {
        return this.sendContentSMS(params);
    }

    private Map<String, Object> createParams(String telephons, String content) {
        Map<String, Object> params = new HashMap<>();
        List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
        params.put("account", this.account);
        params.put("password", this.pswd);

        Map<String, String> data = new HashMap<String, String>();
        data.put("msgid", UUID.randomUUID().toString());
        data.put("phones", telephons);
        data.put("content", content);
        data.put("sign", getFreeSignName());
        datalist.add(data);
        params.put("data", datalist);
        return params;
    }
    private String signName = "";
    @Override
    public String getFreeSignName() {
        return signName;
    }

    public void setFreeSignName(String signName) {
        this.signName = signName;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public void setVoiceAccount(String voiceAccount) {
        this.voiceAccount = voiceAccount;
    }

    public void setVoicePswd(String voicePswd) {
        this.voicePswd = voicePswd;
    }
    
    
    
}
