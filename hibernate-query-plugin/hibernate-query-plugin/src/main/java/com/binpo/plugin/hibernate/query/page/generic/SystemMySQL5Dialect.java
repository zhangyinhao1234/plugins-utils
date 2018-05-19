package com.binpo.plugin.hibernate.query.page.generic;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;

import org.hibernate.type.DateType;
import org.hibernate.type.StringType;
import org.hibernate.type.TextType;

public class SystemMySQL5Dialect extends MySQL5Dialect {
	public SystemMySQL5Dialect() {
        super();
        registerFunction("date_add", new SQLFunctionTemplate(DateType.INSTANCE,
                "date_add(?1, INTERVAL ?2 ?3)"));
        registerHibernateType(-1, TextType.INSTANCE.getName());
        registerFunction("convert_gbk", new SQLFunctionTemplate(StringType.INSTANCE, "convert(?1 using gbk)"));
    }

    /**
     * 
     * 不创建外键
     * 
     * @return
     */
	@Override
    public boolean hasAlterTable() {
        return false;
    }
}