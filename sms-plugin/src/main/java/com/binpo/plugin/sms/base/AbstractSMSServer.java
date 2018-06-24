package com.binpo.plugin.sms.base;

import com.binpo.plugin.sms.base.SMSUtil.SendCode;

public abstract class AbstractSMSServer implements ISMSToolService {
    /**
     * 短信签名
     */
    protected String freeSignName = "淘菜猫";

    /**
     * 系统的默认实现，如果需要单独自己实现可以继承这个类，重写实现方法
     */
    public abstract SendCode sendSMS(SMSSendParams params);
    
    
}
