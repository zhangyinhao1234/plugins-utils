package com.binpo.plugin.sms.base;

import com.binpo.plugin.sms.base.SMSUtil.SendCode;

public abstract class AbstractSMSServer implements ISMSToolService {

    /**
     * 系统的默认实现，如果需要单独自己实现可以继承这个类，重写实现方法
     */
    public abstract SendCode sendSMS(SMSSendParams params);

    /**
     * 
     * 获取短信签名
     * 
     * @return
     */
    public abstract String getFreeSignName();

}
