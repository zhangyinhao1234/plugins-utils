package com.binpo.plugin.hibernate.query.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.binpo.plugin.hibernate.query.strategy.IdService;
import com.binpo.plugin.hibernate.query.strategy.IdWorker;

/**
 * <p>
 * id策略配置，通过配置spring.idworker.strategy的实现类来加载id的实现策略<blockquote>
 * 
 * <pre>
 *     例如：
 *     #redis 分布式配置：node1:127.0.0.1:6379;node2:127.0.0.1:6380
 *     spring.redis.hosts=node1:127.0.0.1:6379
 *     #id策略配置(集成了redisid生成策略 配置redis的策略 需要配置 spring.redis.hosts) 否则使用系统默认的策略
 *     spring.idworker.strategy=com.tcm.framework.core.dao.query.strategy.RedisEntityIdWorker
 * </pre>
 * 
 * </blockquote> 在没有配置的情况下会使用系统默认的id生成策略，{@link IdWorker}
 * 
 * @author zhang
 *
 */
@Configuration
public class IdWorkerConfig {
    private Log logger = LogFactory.getLog(getClass());
    /**
     * id 生成 实现的class
     */
    @Value("${spring.idworker.strategy:}")
    private String idWorkerStrategy;
    /**
     * 
     * 实例化 id 生成的实现
     * 
     * @return
     */
    @Bean
    public IdService entityIdWorker() {
        if (idWorkerStrategy == null || idWorkerStrategy.equals("")) {
            logger.info("加载默认的id策略");
            return new IdWorker();
        }
        try {
            logger.info("加载：" + idWorkerStrategy + "id策略");
            Class<?> forName = Class.forName(idWorkerStrategy);
            Object newInstance = forName.newInstance();
            return (IdService) newInstance;
        } catch (Exception e) {
            logger.error("使用：" + idWorkerStrategy + " id策略失败", e);
        }
        return new IdWorker();
    }
}
