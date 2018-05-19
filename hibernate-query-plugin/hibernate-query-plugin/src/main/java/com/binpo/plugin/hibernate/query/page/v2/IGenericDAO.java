package com.binpo.plugin.hibernate.query.page.v2;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IGenericDAO {
    <T> T get(Class zlass,Serializable paramSerializable);

    <T> void save(T obj);

    <T> void remove(Class zlass,Serializable paramSerializable);

    <T> void update(T paramT);


    List executeNamedQuery(String paramString, Object[] paramArrayOfObject, int paramInt1, int paramInt2);

    int executeNativeSQLMap(final String nnq, Map params);

    List find(Class zlass,String paramString, Map paramMap, int paramInt1, int paramInt2);

    List findNoCache(Class zlass,String query, Map params, int begin, int max);

    List query(String paramString, Map paramMap, int paramInt1, int paramInt2);

    int batchUpdate(String paramString, Object[] paramArrayOfObject);

    int batchUpdate(String paramString, Map paramArrayOfObject);

    List executeNativeNamedQuery(String paramString);

    List executeNativeQuery(String paramString, Object[] paramArrayOfObject, int paramInt1, int paramInt2);

    List executeNativeQuery(String nnq, Map params, int begin, int max);

    int executeNativeSQL(String paramString);

    void flush();
    void clearSessionCache();

    /**
     * HQL取得唯一记录
     * 
     * @param queryStr
     * @param params
     * @return
     *
     * @author Francis
     * @since 2015年9月18日
     */
    Object queryUnique(final String queryStr, final Map params);

    /**
     * SQL取得唯一记录
     * 
     * @param nnq
     * @param params
     * @return
     *
     * @author Francis
     * @since 2015年9月18日
     */
    Object executeNativeQueryUnique(final String nnq, final Map params);

    <T> T getOfSlave(Class zlass,Serializable id);

    Object queryUniqueOfSlave(String queryStr, Map params);

    List queryOfSlave(String query, Map params, int begin, int max);

    List findOfSlave(Class zlass,String query, Map params, int begin, int max);

    List executeNativeQueryOfSlave(String nnq, Map params, int begin, int max);

    List executeNativeQueryOfSlave(String nnq, Object[] params, int begin, int max);

    List executeNativeNamedQueryOfSlave(String nnq);

    List executeNamedQueryOfSlave(String queryName, Object[] params, int begin, int max);

}