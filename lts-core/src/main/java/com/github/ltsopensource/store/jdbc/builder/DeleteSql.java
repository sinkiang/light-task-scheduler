package com.github.ltsopensource.store.jdbc.builder;

import com.github.ltsopensource.core.commons.utils.StringUtils;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.store.jdbc.SQLFormatter;
import com.github.ltsopensource.store.jdbc.SqlTemplate;
import com.github.ltsopensource.store.jdbc.exception.JdbcException;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Robert HG (254963746@qq.com) on 3/9/16.
 */
public class DeleteSql {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteSql.class);

    private SqlTemplate sqlTemplate;
    private StringBuilder sql = new StringBuilder();
    private List<Object> params = new LinkedList<Object>();

    public DeleteSql(SqlTemplate sqlTemplate) {
        this.sqlTemplate = sqlTemplate;
    }

    public DeleteSql delete() {
        sql.append(" DELETE ");
        return this;
    }

    public DeleteSql all() {
        sql.append(" * ");
        return this;
    }

    public DeleteSql from() {
        sql.append(" FROM ");
        return this;
    }

    public DeleteSql table(Delim delim, String table) {
        sql.append(delim.getLeftSpaces()).append(table.trim()).append(delim.getRightSpaces());
        return this;
    }

    public DeleteSql where() {
        sql.append(" WHERE ");
        return this;
    }

    public DeleteSql whereSql(WhereSql whereSql) {
        sql.append(whereSql.getSQL());
        params.addAll(whereSql.params());
        return this;
    }

    public DeleteSql where(String condition, Object value) {
        sql.append(" WHERE ").append(condition);
        params.add(value);
        return this;
    }

    public DeleteSql and(String condition, Object value) {
        sql.append(" AND ").append(condition);
        params.add(value);
        return this;
    }

    public DeleteSql or(String condition, Object value) {
        sql.append(" OR ").append(condition);
        params.add(value);
        return this;
    }

    public DeleteSql and(String condition) {
        sql.append(" AND ").append(condition);
        return this;
    }

    public DeleteSql or(String condition) {
        sql.append(" OR ").append(condition);
        return this;
    }

    public DeleteSql andOnNotNull(String condition, Object value) {
        if (value == null) {
            return this;
        }
        return and(condition, value);
    }

    public DeleteSql orOnNotNull(String condition, Object value) {
        if (value == null) {
            return this;
        }
        return or(condition, value);
    }

    public DeleteSql andOnNotEmpty(String condition, String value) {
        if (StringUtils.isEmpty(value)) {
            return this;
        }
        return and(condition, value);
    }

    public DeleteSql orOnNotEmpty(String condition, String value) {
        if (StringUtils.isEmpty(value)) {
            return this;
        }
        return or(condition, value);
    }

    public DeleteSql andBetween(Delim delim, String column, Object start, Object end) {

        if (start == null && end == null) {
            return this;
        }

        if (start != null && end != null) {
            sql.append(" AND (")
                    .append(delim.get()).append(column).append(delim.get())
                    .append(" BETWEEN ? AND ? ").append(")");
            params.add(start);
            params.add(end);
            return this;
        }

        if (start == null) {
            sql.append(delim.get()).append(column).append(delim.get()).append(" <= ? ");
            params.add(end);
            return this;
        }

        sql.append(delim.get()).append(column).append(delim.get()).append(" >= ? ");
        params.add(start);
        return this;
    }

    public DeleteSql orBetween(Delim delim, String column, Object start, Object end) {

        if (start == null && end == null) {
            return this;
        }

        if (start != null && end != null) {
            sql.append(" OR (").append(delim.get()).append(column).append(delim.get()).append(" BETWEEN ? AND ? ").append(")");
            params.add(start);
            params.add(end);
            return this;
        }

        if (start == null) {
            sql.append(delim.get()).append(column).append(delim.get()).append(" <= ? ");
            params.add(end);
            return this;
        }

        sql.append(delim.get()).append(column).append(delim.get()).append(" >= ? ");
        params.add(start);
        return this;
    }

    public int doDelete() {
        String finalSQL = getSQL();
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(SQLFormatter.format(finalSQL));
            }
            return sqlTemplate.delete(finalSQL, params.toArray());
        } catch (SQLException e) {
            throw new JdbcException("Delete SQL Error:" + SQLFormatter.format(finalSQL), e);
        }
    }

    public String getSQL() {
        return sql.toString();
    }
}
