package com.binpo.plugin.cloud.aws.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.binpo.plugin.cloud.ISecretKey;
import com.binpo.plugin.cloud.aws.AWSFactory;
import com.binpo.plugin.cloud.aws.AWSSecretKey;
import com.binpo.plugin.cloud.aws.s3.AmazonS3Impl;
import com.binpo.plugin.cloud.aws.s3.CredentialsUtil;
import com.binpo.plugin.cloud.storage.ObjClient;

@Configuration
@Component
public class AWSConfig {
    @Value("${aws.key.secretId}")
    private String secretId;

    @Value("${aws.key.secretKey}")
    private String secretKey;

    @Bean
    public ObjClient s3client() {
        AmazonS3Impl amazonS3Impl = new AmazonS3Impl(awsSecretKey());
        AWSFactory.setS3Client(amazonS3Impl);
        return amazonS3Impl;
    }
    @Bean
    public ISecretKey awsSecretKey() {
        AWSSecretKey awsSecretKey = new AWSSecretKey(secretId,secretKey);
        AWSFactory.setKey(awsSecretKey);
        return awsSecretKey;
    }
}
