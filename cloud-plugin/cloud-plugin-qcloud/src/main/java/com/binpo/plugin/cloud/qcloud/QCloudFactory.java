package com.binpo.plugin.cloud.qcloud;

import com.binpo.plugin.cloud.storage.ObjClient;

/**
 * 
 * 腾讯云的实例化工厂
 *
 * @author zhang 2017年6月14日 下午2:46:50
 */
public class QCloudFactory {
    /**
     * 对象存储接口
     */
    private static ObjClient s3Client;

    public static ObjClient getS3Client() {
        return s3Client;
    }

    public static void setS3Client(ObjClient s3Client) {
        QCloudFactory.s3Client = s3Client;
    }

    
}
