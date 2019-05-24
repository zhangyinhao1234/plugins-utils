package com.binpo.plugin.sms.control;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.binpo.plugin.sms.base.ISMSToolService;
import com.binpo.plugin.sms.base.SMSSendParams;
import com.binpo.plugin.sms.base.SMSUtil;
import com.binpo.plugin.sms.base.SMSUtil.SendCode;
import com.binpo.plugin.sms.control.i.SMSConfig;
import com.binpo.plugin.sms.control.i.SMSErrorInfoSend;
import com.binpo.plugin.sms.control.i.SMSSaveContent;
import com.binpo.plugin.sms.control.i.SMSSpringBeanUtil;

/**
 * 短信调度中心， 短信调度中心和短信服务隔离
 * 初始化短信调度中心可以访问数据库获取短信配置和短信服务优先级(采用接口访问数据，实现可能是数据库直接读取或者dubbo服务调用)
 * 通过短信调度中心发送短信，并且在调度中心调用记录日志接口 短信服务商出现异常则将此短信服务商的优先级降低 直到短信发送成功 发送都失败则发送异常邮件
 * 
 * 关于springSmsBeanIds 的集群节点的同步，admin管理修改配置后通知到每一个注册的服务器到redis中加载新的数据
 * 
 * @author zhang
 *
 */
public class SMSControlCenterImpl implements SMSControlCenter {
    protected Boolean inServer = true;

    protected Boolean voiceinServer = true;

    private String defaultSpringids = null;

    protected List<String> springSmsBeanIds = new ArrayList<>();
    /**
     * 保存短信内容接口
     */
    private SMSSaveContent saveContentToDB;
    /**
     * 用于使用其他途径来获取实时的配置信息
     */
    private SMSConfig smsConfig;
    /**
     * 第三方错误日志发送接口
     */
    private SMSErrorInfoSend sendErrorInfo;
    /**
     * spring 上下文工具
     */
    private SMSSpringBeanUtil applicationContextUtil;

    private Log logger = LogFactory.getLog(this.getClass());

    public SMSControlCenterImpl() {

    }

    @Override
    public void sendSMS(SMSSendParams params) {
        loadsmsConfig();
        if (!inServer && !voiceinServer) {
            return;
        }
        if (this.springSmsBeanIds.size() == 0) {
            logger.error("没有短信服务商，无法发送短信");
            sendErrorInfoToDeveloper("没有短信服务商，无法发送短信");
            return;
        }
        if (inServer && params.getType().equals(ISMSToolService.Type.content)) {
            List<String> errorBeans = new ArrayList<>();
            StringBuffer error = toSendSms(errorBeans, params);
            saveSMSContentToDataBase(params, errorBeans.size());
            sendErrorInfo(error, errorBeans);
            updateSmsPriority(errorBeans);
        }
    }

    private void loadsmsConfig() {
        if (smsConfig != null) {
            this.inServer = this.smsConfig.smsServerIsOpen();
            this.voiceinServer = this.smsConfig.smsVoiceServerIsOpen();
            List<String> springBeanIds = this.smsConfig.getSpringBeanIds();
            if (springBeanIds != null && springBeanIds.size() > 0) {
                this.springSmsBeanIds = springBeanIds;
            }
        }
    }

    private StringBuffer toSendSms(List<String> errorBeans, SMSSendParams params) {
        logger.debug("目标手机：" + params.getTelephone());
        logger.debug("短信内容：" + params.getContent());
        StringBuffer error = new StringBuffer();
        if (applicationContextUtil != null) {
            for (String beanId : this.springSmsBeanIds) {
                Object bean = applicationContextUtil.getBean(beanId);
                if (bean == null) {
                    error.append("短信服务配置 spring bean 异常，找不到 id 为" + beanId + "的短信服务接口");
                    continue;
                }
                ISMSToolService smsToolSerice = (ISMSToolService) bean;
                SendCode code = SMSUtil.SendCode.SMS_IS_OK;
                if (params.getType().equals(ISMSToolService.Type.content)) {
                    code = smsToolSerice.sendSMS(params);
                } else {
                    code = smsToolSerice.sendVoiceSecurityCode(params);
                }
                if (code != null && code.equals(SendCode.SMS_IS_OK)) {
                    logger.debug("使用：" + smsToolSerice.getSMSServiceName() + "短信平台发送短信成功");
                    break;
                } else {
                    logger.error(smsToolSerice.getSMSServiceName() + "：短信服务商异常,异常原因" + code + "短信内容："
                            + params.getContent());
                    error.append(smsToolSerice.getSMSServiceName() + "：短信服务商异常,异常原因" + code + "短信内容："
                            + params.getContent() + "||");
                    errorBeans.add(beanId);
                }
            }
        } else {
            sendErrorInfoToDeveloper(SMSControlCenterImpl.class + "  applicationContextUtil 为 null ");
        }
        return error;
    }

    private void saveSMSContentToDataBase(SMSSendParams params, int errorCount) {
        if (saveContentToDB != null) {
            Boolean sendSuccess = true;
            if (errorCount == this.springSmsBeanIds.size()) {
                sendSuccess = false;
            }
            saveContentToDB.save(sendSuccess, params.getTelephone(), params.getContent(),
                    params.getSmsTemplateCode(), params.getParams());
        }
    }

    private void sendErrorInfo(StringBuffer error, List<String> errorBeans) {
        if (errorBeans.size() == 0) {
            return;
        }
        if (error.toString().length() > 0) {
            sendErrorInfoToDeveloper(error.toString());
        }

    }

    private void sendErrorInfoToDeveloper(String errorInfo) {
        if (sendErrorInfo != null) {
            sendErrorInfo.send(errorInfo);
        }
    }

    private void updateSmsPriority(List<String> errorBeans) {
        if (errorBeans.size() > 0) {
            this.springSmsBeanIds.removeAll(errorBeans);
            this.springSmsBeanIds.addAll(errorBeans);
        }
    }

    @Override
    public boolean closeSMSServer() {
        inServer = false;
        return true;
    }

    @Override
    public boolean openSMSServer() {
        inServer = true;
        return true;
    }

    public SMSSaveContent getSaveContentToDB() {
        return saveContentToDB;
    }

    public void setSaveContentToDB(SMSSaveContent saveContentToDB) {
        this.saveContentToDB = saveContentToDB;
    }

    public SMSConfig getSmsConfig() {
        return smsConfig;
    }

    public void setSmsConfig(SMSConfig smsConfig) {
        this.smsConfig = smsConfig;
    }

    public String getDefaultSpringids() {
        return defaultSpringids;
    }

    public SMSErrorInfoSend getSendErrorInfo() {
        return sendErrorInfo;
    }

    public void setSendErrorInfo(SMSErrorInfoSend sendErrorInfo) {
        this.sendErrorInfo = sendErrorInfo;
    }

    public void setApplicationContextUtil(SMSSpringBeanUtil applicationContextUtil) {
        this.applicationContextUtil = applicationContextUtil;
    }

    public void setDefaultSpringids(String defaultSpringids) {
        this.defaultSpringids = defaultSpringids;
        if (defaultSpringids != null) {
            String[] ids = defaultSpringids.split(",");
            this.springSmsBeanIds.clear();
            for (String id : ids) {
                this.springSmsBeanIds.add(id);
            }
        }
    }

}
