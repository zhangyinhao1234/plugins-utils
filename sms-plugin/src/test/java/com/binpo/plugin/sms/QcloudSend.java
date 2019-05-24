package com.binpo.plugin.sms;

import java.util.ArrayList;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;


public class QcloudSend {

    public static void main(String[] args) {
        try {
            //请根据实际 appid 和 appkey 进行开发，以下只作为演示 sdk 使用
            int appid = 1400032527;
            String appkey = "5d9f372bebbb760cde3d5905fd31d6bb";
            
            String phoneNumber1 = "15262516056";
//            String phoneNumber2 = "12345678902";
//            String phoneNumber3 = "12345678903";
            int tmplId = 7839;

             //初始化单发
            SmsSingleSender singleSender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult singleSenderResult;
    
             //普通单发
            singleSenderResult = singleSender.send(0, "86", phoneNumber1, "您的手机验证码是：" + "123456" + ",打死都不能告诉别人哦！(15分钟有效)", "", "");
//            SmsSingleSenderResult
//            result 0
//            errMsg OK
//            ext 
//            sid 8:coiBXL66Ndlv4an67SN20170602
//            fee 1

            System.out.println(singleSenderResult);
    
//             //指定模板单发
//             //假设短信模板 id 为 1，模板内容为：测试短信，{1}，{2}，{3}，上学。
//            ArrayList<String> params = new ArrayList<>();
//            params.add("指定模板单发");
//            params.add("深圳");
//            params.add("小明");
//            singleSenderResult = singleSender.sendWithParam("86", phoneNumber2, tmplId, params, "", "", "");
//            System.out.println(singleSenderResult);
//            
//            // 初始化群发
//            SmsMultiSender multiSender = new SmsMultiSender(appid, appkey);
//            SmsMultiSenderResult multiSenderResult;
//    
//            // 普通群发
//            // 下面是 3 个假设的号码
//            ArrayList<String> phoneNumbers = new ArrayList<>();
//            phoneNumbers.add(phoneNumber1);
//            phoneNumbers.add(phoneNumber2);
//            phoneNumbers.add(phoneNumber3);
//            multiSenderResult = multiSender.send(0, "86", phoneNumbers, "测试短信，普通群发，深圳，小明，上学。", "", "");
//            System.out.println(multiSenderResult);
//
//            // 指定模板群发
//            // 假设短信模板 id 为 1，模板内容为：测试短信，{1}，{2}，{3}，上学。
//            params = new ArrayList<>();
//            params.add("指定模板群发");
//            params.add("深圳");
//            params.add("小明");
//            multiSenderResult = multiSender.sendWithParam("86", phoneNumbers, tmplId, params, "", "", "");
//            System.out.println(multiSenderResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
}
}
