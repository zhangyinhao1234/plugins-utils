package com.binpo.plugin.hibernate.query.page.support;

import java.util.Map;

public interface IQueryObject {
    String getQuery();

    Map getParameters();

    PageObject getPageObj();

    IQueryObject addQuery(String scope, Map paramMap);

    IQueryObject addQuery(String field, SysMap sysMap, String expression);

    IQueryObject addQuery(String para, Object obj, String field,String expression);
}