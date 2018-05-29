package com.binpo.plugin.cloud;

import com.binpo.plugin.cloud.queue.IMessageQueue;
import com.binpo.plugin.cloud.storage.ObjClient;
import com.binpo.plugin.cloud.topic.ITopic;

/**
 * 
 * 抽象的云工厂
 *
 * @author zhang 2017年6月14日 下午1:02:28
 */
public interface ICloudFactoryService {

    /**
     * 
     * 获取对象存储服务
     * 
     * @return
     */
    ObjClient getStorageService();

    /**
     * 
     * 获取对象存储服务
     * 
     * @return
     */
    ObjClient getStorageService(String id);

    /**
     * 
     * 消息队列获取
     * 
     * @param id
     * @return
     */
    IMessageQueue getQueue(String id);

    /**
     * 
     * 获取topic
     * 
     * @param id
     * @return
     */
    ITopic getTopic(String id);

}
