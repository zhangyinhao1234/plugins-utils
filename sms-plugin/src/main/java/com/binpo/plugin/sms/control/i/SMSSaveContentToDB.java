package com.binpo.plugin.sms.control.i;

import java.util.Map;

/**
 * 保存短信内容接口
 * 
 * @author zhang
 *
 */
public interface SMSSaveContentToDB {
    public abstract void save(Boolean sendSuccess, String telephone, String content, String smsTemplateCode,
            Map<String, String> params);
}
