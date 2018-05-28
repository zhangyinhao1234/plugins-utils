package com.github.zhangyinhao1234.plugin.wechat_plugin;

import java.util.HashMap;
import java.util.Map;

import com.binpo.plugin.wechat.mp.template.WeChatTMessage;
import com.binpo.plugin.wechat.mp.template.bean.TMessage;
import com.binpo.plugin.wechat.mp.template.bean.TValue;

public class TemplateMsgTest {

    public static void main(String[] a){
        String accessToken = "-_4bKD4BqczDx5qtXC9arpEqGbNn8DRPrRMK3jaIPwoZGctgR3k4FlKD6bmlbE3ekoffPpa_WLEkk3qL4N8ml-xRTISjAEAZOY";
        String touser="";
        String template_id = "-4jXejVbd__s0ZA";
        Map<String,TValue> data = new HashMap<String,TValue>();
        data.put("name", new TValue("淘菜猫优品"));
        data.put("remark", new TValue("好的品质，从优品开始"));
        TMessage tMessage = new TMessage(touser, template_id, data);
        
        WeChatTMessage.sendMessage(tMessage, accessToken);
    }
}
