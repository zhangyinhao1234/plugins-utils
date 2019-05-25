# 插件使用说明

## hibernate-query-plugin

​	提供常用的数据查询，修改，查询，分页查询接口，简化在使用Hibernate的配置，只需要配置数据连接和entity的扫描包即可使用，支持直接在spring boot中使用。详细的文档查看

 [hibernate-query-plugin使用说明](https://github.com/zhangyinhao1234/plugins-utils/tree/master/hibernate-query-plugin)



## wechat-plugin

​	对微信公众号中的授权进行处理，可以生成针对统一域名的授权转发，从重定向的地址中获取的用户信息，微信会传递一个code回来，通过code获取accessToken，来获取用户的信息。

​	微信公众号的模板消息发送，提供openid，模板id，appid，和appSecret，入参调用方法即可发送模板消息。

[代码示例](https://github.com/zhangyinhao1234/plugins-utils/tree/master/wechat-plugin)



## cloud-plugin

​	对AWS和Qcloud的消息队列，订阅通知，文件上传设置了统一的接口，分别对定义的接口进行了实现，方便在项目中直接使用文件上传，消息发送订阅。可以在一个项目中定义多个文件上传的桶，消息队列，可以同时使用AWS和Qcloud的服务，通过接口的方式，方便日后进行实现的替换（例如从AWS迁移到腾讯云）

[代码示例](https://github.com/zhangyinhao1234/plugins-utils/tree/master/cloud-plugin)



## sms-plugin

​	发送短信的核心组件，默认实现了示远科技，大汉三通和腾讯云的短信发送。实现多供应商的短信接口，当有任何一方短信发送失败后会使用另外的短信平台发送短信。

​	对扩展支持：如果想增加其他短信供应商，可以继承 AbstractSMSServer 抽象类。轻松替换/增加短息平台

[代码示例](https://github.com/zhangyinhao1234/spring-boot-starter-sms)

