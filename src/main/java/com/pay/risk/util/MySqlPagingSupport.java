/*
 * 文 件 名:  MySqlPagingSupport.java
 * 版    权:  支付有限公司. Copyright 2011-2014,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  tao.zhang
 * 修改时间:  2014-12-15
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.pay.risk.util;

import net.mlw.vlh.adapter.jdbc.util.SqlPagingSupport;

/**
 * ValueList MySQL paging support of custom
 * <p>
 * @author tao.zhang
 * @version [V1.0, 2014-12-15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MySqlPagingSupport extends SqlPagingSupport {

    public static final String MYSQL = "mysql";

    private String pagedQueryPreSql;
    private String pagedQueryPostSql;

    /**
	 *
	 * */
    @Override
    public StringBuffer getPagedQuery(String sql) {
        StringBuffer buffer = new StringBuffer(500);
        buffer.append(pagedQueryPreSql);
        buffer.append(sql);
        buffer.append(pagedQueryPostSql);

        return buffer;
    }

    /**
	 *
	 * */
    @Override
    public StringBuffer getCountQuery(String sql) {
        StringBuffer buffer = new StringBuffer(sql.length() + 100);
        return buffer.append("select count(*) from (").append(sql).append(") as t ");
    }

    /**
	 *
	 * */
    @Override
    public void setDatabase(String database) {
        if (MYSQL.equalsIgnoreCase(database)) {
            setPagedQueryPreSql("select * from (");
            setPagedQueryPostSql(") as t limit [StartIndex],[pagingNumberPer] ");
        } else {
            throw new NullPointerException(database + " is not supported (" + MYSQL + ").");
        }
    }

    @Override
    public void setPagedQueryPostSql(String pagedQueryPostSql) {
        this.pagedQueryPostSql = pagedQueryPostSql;
    }

    @Override
    public void setPagedQueryPreSql(String pagedQueryPreSql) {
        this.pagedQueryPreSql = pagedQueryPreSql;
    }

}
