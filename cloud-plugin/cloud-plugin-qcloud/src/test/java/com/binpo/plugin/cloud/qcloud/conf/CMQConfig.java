package com.binpo.plugin.cloud.qcloud.conf;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.binpo.plugin.cloud.ISecretKey;
import com.binpo.plugin.cloud.qcloud.QCloud.QCloudRegion;
import com.binpo.plugin.cloud.qcloud.cmq.CMQQueueClient;
import com.binpo.plugin.cloud.qcloud.util.BspAPI.QcloudHttpType;
import com.binpo.plugin.cloud.queue.IMessageQueue;

@Component
@Configurable
public class CMQConfig {

    @Bean("testqueue")
    public IMessageQueue testqueue(ISecretKey secretKey) {
        String queueName = "sellerBalanceDev";
        return new CMQQueueClient(QCloudRegion.sh, queueName,QcloudHttpType.https, secretKey);
    }
}
