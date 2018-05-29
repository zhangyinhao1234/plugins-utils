package com.binpo.plugin.cloud.aws.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

/**
 * 
 * 后续上传图片建议使用
 * {@link com.binpo.plugin.cloud.aws.taocaimall.util.AWSFactory}进行图片上传，例子可以参考{@link S3Example}
 *
 * @author zhang 2017年6月9日 下午8:45:57
 */
public class CredentialsUtil {

    /**
     * @return AWSCredentials
     */
    public static AWSCredentials getCredentials(String secretId,String secretKey) {
        AWSCredentials credentials = new BasicAWSCredentials(secretId, secretKey);
        return credentials;
    }

}
