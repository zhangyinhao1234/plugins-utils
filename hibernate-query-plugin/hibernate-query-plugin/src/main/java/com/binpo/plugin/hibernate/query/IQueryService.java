package com.binpo.plugin.hibernate.query;

import java.util.List;
import java.util.Map;

public abstract interface IQueryService {
    /**
     * 纯sql的查询 不需要通过hibernate进行sql解释
     * 
     * @param sql 例如：select obj.id,obj.userName from iskyshop_user obj where
     *            obj.userName=:userName
     * @param params 参数
     * @return
     */
    List<Map<String, Object>> queryWithSQL(String sql, Map<String, ?> params);

    /**
     * 
     * 通用查询
     * 
     * @param zlass 查询的对象实体
     * @param queryStr 查询语句
     * @param params 查询参数
     * @param begin 开始行
     * @param max 查询数量
     * @param <T> T extends {@link IdEntity}
     * @return
     */
    <T> List<T> query(Class<T> zlass, String queryStr, Map params, int begin, int max);
    
    List query(String scope, Map params, int begin, int max);

    /**
     * 
     * 获取对象
     * 
     * @param zlass 查询的对象实体
     * @param id id
     * @param <T> T extends {@link IdEntity}
     * @return
     */
    <T> T getObj(Class<T> zlass, Long id);

    /**
     * 
     * 获取一个对象
     * 
     * @param queryStr 查询语句
     * @param params 查询参数
     * @return
     */
    Object queryUnique(String queryStr, Map params);

   

}