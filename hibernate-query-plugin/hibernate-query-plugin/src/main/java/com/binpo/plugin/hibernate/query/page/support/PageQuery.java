package com.binpo.plugin.hibernate.query.page.support;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * 原生sql拼接实现
 *
 * @author zhang 2017年9月30日 下午5:53:13
 */
public class PageQuery implements IPageQuery{
    
    protected Integer pageSize = Integer.valueOf(16);
    protected Integer currentPage = Integer.valueOf(0);
    protected Map params = new HashMap();
    protected StringBuffer sql = new StringBuffer();
    
    public PageQuery(){}
    
    public PageQuery(String sql,String currentPage){
        this.addQuery(sql);
        currentPage(currentPage);
    }

    public PageQuery(String sql, String currentPage, Map params) {
        this.addQuery(sql);
        currentPage(currentPage);
        if (params != null) {
            this.params = params;
        }
    }
    
    
    public PageQuery(String currentPage) {
        currentPage(currentPage);
    }

    private void currentPage(String currentPage){
        if ((currentPage == null) || (currentPage.equals(""))) {
            setCurrentPage(0);
            setPageSize(-1);
        }else{
            setCurrentPage(Integer.valueOf(null2Int(currentPage)));
            setPageSize(this.pageSize);
        }
    }
    

    @Override
    public String getQuery() {
        return sql.toString();
    }

    @Override
    public Map getParameters() {
        return this.params;
    }

    @Override
    public PageObject getPageObj() {
        PageObject pageObj = new PageObject();
        pageObj.setCurrentPage(getCurrentPage());
        pageObj.setPageSize(getPageSize());
        if ((this.currentPage == null) || (this.currentPage.intValue() <= 0)) {
            pageObj.setCurrentPage(Integer.valueOf(1));
        }
        return pageObj;
    }

    @Override
    public IPageQuery addQuery(String sql) {
        this.sql.append(sql);
        return this;
    }

    @Override
    public IPageQuery addQuery(String sql, Map paramMap) {
        this.sql.append(" ");
        this.sql.append(sql);
        this.sql.append(" ");
        this.params.putAll(paramMap);
        return this;
    }
    @Override
    public IPageQuery addQuery(String sql, SysMap sysMap) {
        this.sql.append(" ");
        this.sql.append(sql);
        this.sql.append(" ");
        this.params.put(sysMap.getKey(), sysMap.getValue());
        return this;
    }
    
    
    private int null2Int(Object s) {
        int v = 0;
        if (s != null)
            try {
                v = Integer.parseInt(s.toString());
            } catch (Exception localException) {
            }
        return v;
    }
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    



}
