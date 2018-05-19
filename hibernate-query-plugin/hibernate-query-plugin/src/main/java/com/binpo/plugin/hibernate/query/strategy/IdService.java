package com.binpo.plugin.hibernate.query.strategy;

/**
 * 数据库id生成策略
 * 
 * @author zhang
 *
 */
public interface IdService {
    /**
     * 
     * 获取id
     * 
     * @param zlass CLASS
     * @return
     */
    Long getId(Class<?> zlass);
}
