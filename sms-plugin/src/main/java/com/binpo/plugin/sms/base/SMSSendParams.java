package com.binpo.plugin.sms.base;

import java.util.HashMap;
import java.util.Map;

public class SMSSendParams {
    private String telephone;

    private String content;

    private String smsTemplateCode;
    private Map<String, String> params = new HashMap<String, String>();

    private String type = ISMSToolService.Type.content;// voice 内容短信还是语音验证码

    public SMSSendParams(String telephone, String content) {
        this.telephone = telephone;
        this.content = content;
    }

    public SMSSendParams(String telephone, String content, String type) {
        this.telephone = telephone;
        this.content = content;
    }

    public SMSSendParams(String smsTemplateCode, Map<String, String> params) {
        this.smsTemplateCode = smsTemplateCode;
        if (params != null) {
            this.params = params;
        }
    }

    public String getType() {
        return type;
    }

    /**
     * ISMSToolService.Type
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getSmsTemplateCode() {
        return smsTemplateCode;
    }

    public void setSmsTemplateCode(String smsTemplateCode) {
        this.smsTemplateCode = smsTemplateCode;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
