package com.binpo.plugin.wechat.mp.template.bean;
/**
 * 
 * 模板消息中的值
 *
 * @author zhang 2017年7月14日 上午11:25:09
 */
public class TValue {

    private String value="";
    
    private String color = "#173177";
    
    public TValue(String value){
        this.value = value;
    }
    public TValue(String value,String color){
        this.value = value;
        this.color = color;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }
    
    
}
