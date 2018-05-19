package com.binpo.plugin.hibernate.query.page.v2;

import java.util.List;
import java.util.Map;

import com.binpo.plugin.hibernate.query.page.support.IQuery;

/**
 * 
 * 查询
 *
 * @author zhang 2017年4月19日 下午4:21:27
 */
public class GenericQuery implements IQuery {
    /**
     * 查询的dao
     */
    private IGenericDAO dao;
    /**
     * 需要查询的实体对象
     */
    private Class cls;
    /**
     * 查询开始
     */
    private int begin;
    /**
     * 查询的数量
     */
    private int max;
    /**
     * 查询使用的条件参数
     */
    private Map params;

    public GenericQuery(IGenericDAO dao,Class cls) {
        this.dao = dao;
        this.cls = cls;
    }

    @Override
    public List getResultNoCache(String condition) {
        return this.dao.findNoCache(this.cls,condition, this.params, this.begin, this.max);
    }

    public List getResult(String condition) {
        return this.dao.find(this.cls,condition, this.params, this.begin, this.max);
    }

    @Override
    public List getResultOfSlave(String condition) {
        return this.dao.findOfSlave(this.cls,condition, this.params, this.begin, this.max);
    }

    public List getResult(String condition, int begin, int max) {
        Object[] params = null;
        return this.dao.find(this.cls,condition, this.params, begin, max);
    }

    @Override
    public List getResultUseQuery(String queryString) {
        Object[] params = null;
        return this.dao.query(queryString, this.params, begin, max);
    }

    @Override
    public List getResultUseQueryOfSlave(String queryString) {
        Object[] params = null;
        return this.dao.queryOfSlave(queryString, this.params, begin, max);
    }

    public int getRows(String condition) {
        int n = condition.toLowerCase().indexOf("order by");
        Object[] params = null;
        if (n > 0) {
            condition = condition.substring(0, n);
        }

        List ret = this.dao.query(condition, this.params, 0, 0);
        if ((ret != null) && (ret.size() > 0)) {
            return ((Long) ret.get(0)).intValue();
        }

        return 0;
    }

    @Override
    public int getRowsOfSlave(String condition) {
        int n = condition.toLowerCase().indexOf("order by");
        Object[] params = null;
        if (n > 0) {
            condition = condition.substring(0, n);
        }

        List ret = this.dao.queryOfSlave(condition, this.params, 0, 0);
        if ((ret != null) && (ret.size() > 0)) {
            return ((Long) ret.get(0)).intValue();

        }

        return 0;
    }

    public void setFirstResult(int begin) {
        this.begin = begin;
    }

    public void setMaxResults(int max) {
        this.max = max;
    }

    public void setParaValues(Map params) {
        this.params = params;
    }
}