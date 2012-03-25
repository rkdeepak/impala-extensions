package org.impalaframework.extension.dataaccess.jdbc;

import org.springframework.util.Assert;

/**
 * Immutable data holder for arguments to JDBC query, representing a combination of SQL and 
 * arguments in the form of an object array. Note the SQL parameters are represented in the form of a
 * ?, and the arguments must be in the correct order.
 * 
 * @author Phil Zoio
 */
public class JdbcArguments {

    private final String sql;

    private final Object[] arguments;

    public JdbcArguments(String sql, Object[] arguments) {
        super();
        Assert.notNull(sql);
        Assert.notNull(arguments);
        this.arguments = arguments;
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getArguments() {
        return arguments;
    }

}
