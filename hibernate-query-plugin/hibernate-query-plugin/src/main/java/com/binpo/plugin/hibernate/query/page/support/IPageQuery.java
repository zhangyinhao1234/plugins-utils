package com.binpo.plugin.hibernate.query.page.support;

import java.util.Map;

/**
 * 
 * 原生sql进行分页的查询
 *
 * @author zhang 2017年9月30日 下午5:45:41
 */
public interface IPageQuery {
    
    String getQuery();

    Map getParameters();

    PageObject getPageObj();

    IPageQuery addQuery(String sql);
    
    IPageQuery addQuery(String sql, Map paramMap);
    
    IPageQuery addQuery(String sql, SysMap sysMap);
}
