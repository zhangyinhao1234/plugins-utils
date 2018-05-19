package com.binpo.plugin.hibernate.query.page.v2;

import java.util.Map;

import com.binpo.plugin.hibernate.query.page.support.IQuery;
import com.binpo.plugin.hibernate.query.page.support.IQueryObject;
import com.binpo.plugin.hibernate.query.page.support.PageList;
/**
 * 
 * 分页查询
 *
 * @author zhang 2017年4月19日 下午3:58:57
 */
public class GenericPageList extends PageList {
    private static final long serialVersionUID = 6730593239674387757L;
    /**
     * 查询语句
     */
    private String scope;
    /**
     * 查询的类
     */
    private Class cls;

    public GenericPageList(Class cls, IQueryObject queryObject, IGenericDAO dao) {
        this(cls, queryObject.getQuery(), queryObject.getParameters(), dao);
    }

    public GenericPageList(Class cls, String scope, Map paras, IGenericDAO dao) {
        this.cls = cls;
        this.scope = scope;
        IQuery query = new GenericQuery(dao,cls);
        query.setParaValues(paras);
        setQuery(query);
    }
    /**
     * 
     * 不从缓存里面进行查询
     * 
     * @param currentPage 查询的页
     * @param pageSize 每页X条
     */
    public void doListNoCache(int currentPage, int pageSize) {
        String totalSql = "select COUNT(obj) from " + this.cls.getName() + " obj where " + this.scope;
        super.doListNoCache(pageSize, currentPage, totalSql, this.scope);
    }
    /**
     * 
     * 不从缓存里面进行查询
     * 
     * @param currentPage 查询的页
     * @param pageSize 每页X条
     */
    public void doList(int currentPage, int pageSize) {
        String totalSql = "select COUNT(obj) from " + this.cls.getName() + " obj where " + this.scope;
        super.doList(pageSize, currentPage, totalSql, this.scope);
    }
    /**
     * 
     * 不从缓存里面进行查询
     * 
     * @param currentPage 查询的页
     * @param pageSize 每页X条
     */
    public void doListOfSlave(int currentPage, int pageSize) {
        String totalSql = "select COUNT(obj) from " + this.cls.getName() + " obj where " + this.scope;
        super.doListOfSlave(pageSize, currentPage, totalSql, this.scope);
    }
}