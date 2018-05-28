package com.binpo.plugin.wechat.mp.template.bean;
/**
 * 
 * 消息推送返回结果
 *
 * @author zhang 2017年7月14日 下午12:00:19
 */
public class TMessageResult {

    private String errcode = "-1";
    
    private String errmsg ="无返回结果";
    
    private String msgid;

    /**
     * @return the errcode
     */
    public String getErrcode() {
        return errcode;
    }

    /**
     * @param errcode the errcode to set
     */
    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    /**
     * @return the errmsg
     */
    public String getErrmsg() {
        return errmsg;
    }

    /**
     * @param errmsg the errmsg to set
     */
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    /**
     * @return the msgid
     */
    public String getMsgid() {
        return msgid;
    }

    /**
     * @param msgid the msgid to set
     */
    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
    
    
    
}
