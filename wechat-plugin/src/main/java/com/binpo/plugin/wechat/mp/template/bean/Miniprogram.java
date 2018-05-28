package com.binpo.plugin.wechat.mp.template.bean;
/**
 * 
 * 模板消息：需要跳转到的小程序信息
 *
 * @author zhang 2018年5月27日 上午10:31:38
 */
public class Miniprogram {

    /**
     * 所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
     */
    private String appid;
    
    /**
     * 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），暂不支持小游戏
     */
    private String pagepath;

    public Miniprogram () {}
    
    public Miniprogram(String appid,String pagepath) {
        this.appid = appid;
        this.pagepath = pagepath;
    }
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }
    
    
}
