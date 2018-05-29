package com.binpo.plugin.cloud.aws;

import com.binpo.plugin.cloud.ISecretKey;
import com.binpo.plugin.cloud.storage.ObjClient;

public class AWSFactory {

    private static ObjClient s3Client;
    
    private static ISecretKey key;

    public static ObjClient getS3Client() {
        return s3Client;
    }

    public static void setS3Client(ObjClient s3Client) {
        AWSFactory.s3Client = s3Client;
    }

    public static ISecretKey getKey() {
        return key;
    }

    public static void setKey(ISecretKey key) {
        AWSFactory.key = key;
    }

}
