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
 * 示远科技 短信提供商
 * 
 * @author zhang
 *
 */
public class SYKJSMS extends AbstractSMSServer {
    private String smsServiceName = "示远科技";
    // private String url="http://send.18sms.com/msg/HttpBatchSendSM";
    private final String batchurl = "http://send.18sms.com/msg/HttpBatchSendSM";
    /**
     * 备用地址
     */
    private final String barchurl_bak = "http://send.18sms.com/msg/HttpBatchSendSM";

    private String account = "111111";
    private String pswd = "password!!";

    private String voiceAccount = "111111";
    private String voicePswd = "password!!!!";
    private int sendCount = 50000;

    private Log logger = LogFactory.getLog(SYKJSMS.class);

    @Override
    public String getSMSServiceName() {
        return smsServiceName;
    }

    @Override
    public SendCode sendSMS(SMSSendParams params) {
        return sendContentSMS(params);
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
                Map<Object, Object> httpParams = this.createParams(subTelephone, params.getContent());
                try {
                    String send = SMSUtil.send(SMSUtil.createParams(httpParams), batchurl, null);
                    String[] split = send.split(",");
                    if (split.length > 1) {
                        String code = split[1];
                        if (!code.equals("0")) {
                            logger.error(smsServiceName + "：异常：" + send + "短信内容：" + params.getContent());
                            return SendCode.SMS_IS_ERROR;
                        }
                    } else {
                        if (send.equals("error")) {
                            return sendofBack(httpParams, params);
                        }
                        return SendCode.SMS_IS_ERROR;
                    }
                } catch (Exception e) {
                    logger.error(e);
                    return sendofBack(httpParams, params);
                }
            }
        }
        return SendCode.SMS_IS_OK;
    }

    private SendCode sendofBack(Map<Object, Object> httpParams, SMSSendParams params) {
        try {
            String send = SMSUtil.send(SMSUtil.createParams(httpParams), barchurl_bak, null);
            String[] split = send.split(",");
            if (split.length > 1) {
                String code = split[1];
                if (!code.equals("0")) {
                    logger.error(smsServiceName + "：异常：" + send + "短信内容：" + params.getContent());
                    return SendCode.SMS_IS_ERROR;
                }
            } else {
                return SendCode.SMS_IS_OK;
            }
        } catch (Exception e) {
            logger.error(e);
            return SendCode.SMS_IS_ERROR;
        }
        return SendCode.SMS_IS_OK;
    }

    @Override
    public SendCode sendVoiceSecurityCode(SMSSendParams params) {
        String url = "http://send.18sms.com/msg/HttpBatchSendSM?";
        String requestinfo = "";
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("account", this.voiceAccount);
        hashMap.put("pswd", this.voicePswd);
        hashMap.put("mobile", params.getTelephone());
        hashMap.put("msg", "您的验证码是：" + params.getContent());
        hashMap.put("needstatus", "true");
        try {
            requestinfo = SMSUtil.send(SMSUtil.createParams(hashMap), url, null);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error(e1);
            return SendCode.SMS_IS_ERROR;
        }
        String result = getResult(requestinfo);
        if (!result.equals("0")) {
            logger.error(smsServiceName + "：异常：" + result + "短信内容：" + params.getContent());
            return SendCode.SMS_IS_ERROR;
        }
        return SendCode.SMS_IS_OK;
    }

    /**
     * 0 提交成功 101 无此用户 102 密码错 103 提交过快（提交速度超过流速限制） 104 系统忙（因平台侧原因，暂时无法处理提交的内容）
     * 105 敏感内容（内容包含敏感词） 108 手机号码个数错（群发>50000或<=0） 109 无发送额度（该用户可用语音数已使用完） 110
     * 不在发送时间内(验证码7*24小时发送) 111 超出该账户当月发送额度限制 112 无此产品，用户没有订购该产品 113
     * extno格式错（非数字或者长度不对） 115 自动审核驳回 117 IP地址认证错,请求调用的IP地址不是系统登记的IP地址 118
     * 用户没有相应的发送权限 119 用户已过期 120 内容不在白名单中
     * 
     * @param result
     * @return
     */
    private String getResult(String result) {
        String[] split = result.split("\\n");
        return split[0].split(",")[1];
    }

    private Map<Object, Object> createParams(String telephons, String content) {
        Map<Object, Object> params = new HashMap<>();
        params.put("account", this.account);
        params.put("pswd", this.pswd);
        params.put("mobile", telephons);
        params.put("msg", content);
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
