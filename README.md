# 插件使用说明

## hibernate-query-plugin

​	提供常用的数据查询，修改，查询，分页查询接口，简化在使用Hibernate的配置，只需要配置数据连接和entity的扫描包即可使用，支持直接在spring boot中使用。详细的文档查看

 [hibernate-query-plugin使用说明](https://github.com/zhangyinhao1234/plugins-utils/tree/master/hibernate-query-plugin)



## wechat-plugin

​	对微信公众号中的授权进行处理，可以生成针对统一域名的授权转发，从重定向的地址中获取的用户信息，微信会传递一个code回来，通过code获取accessToken，来获取用户的信息。

​	微信公众号的模板消息发送，提供openid，模板id，appid，和appSecret，入参调用方法即可发送模板消息。

[代码示例](https://github.com/zhangyinhao1234/plugins-utils/tree/master/wechat-plugin)