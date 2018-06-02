package com.binpo.plugin.cloud.qcloud.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.binpo.plugin.cloud.ISecretKey;
import com.binpo.plugin.cloud.qcloud.QCloudFactory;
import com.binpo.plugin.cloud.qcloud.QCloud.QCloudRegion;
import com.binpo.plugin.cloud.qcloud.cos.COSClient;
import com.binpo.plugin.cloud.storage.ObjClient;

@Configuration
@Component
public class QCloudConfig {
    @Value("${qcloud.key.secretId}")
    private String secretId;

    @Value("${qcloud.key.secretKey}")
    private String secretKey;

    @Value("${qcloud.default.region}")
    private QCloudRegion region;

    @Value("${qcloud.default.bucketName}")
    private String bucketName;

    @Value("${qcloud.default.appId}")
    private long appId;

    @Bean
    public ObjClient cos() {
        COSClient cosClient = new COSClient(region, bucketName, appId, qcloudSecretKey());
        QCloudFactory.setS3Client(cosClient);
        return cosClient;
    }

    @Bean
    public ISecretKey qcloudSecretKey() {
        return new QCloudKey(secretId, secretKey);
    }
}
