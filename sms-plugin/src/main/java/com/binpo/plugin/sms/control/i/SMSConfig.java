package com.binpo.plugin.sms.control.i;

import java.util.List;

public interface SMSConfig {
    public List<String> getSpringBeanIds();

    public Boolean smsServerIsOpen();

    public Boolean smsVoiceServerIsOpen();
}
