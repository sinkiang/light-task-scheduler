package com.github.ltsopensource.store.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.commons.file.FileUtils;
import com.github.ltsopensource.core.constant.Constants;
import com.github.ltsopensource.core.exception.LtsRuntimeException;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.store.jdbc.exception.JdbcException;

/**
 * @author Robert HG (254963746@qq.com) on 5/19/15.
 */
public abstract class JdbcAbstractAccess {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcAbstractAccess.class);

    private SqlTemplate sqlTemplate;

    public JdbcAbstractAccess(Config config) {
        this.sqlTemplate = SqlTemplateFactory.create(config);
    }

    public SqlTemplate getSqlTemplate() {
        return sqlTemplate;
    }

    protected String readSqlFile(String path) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
        try {
            return FileUtils.read(is, Constants.CHARSET);
        } catch (IOException e) {
            throw new LtsRuntimeException("Read sql file : [" + path + "] error ", e);
        }
    }

    protected String readSqlFile(String path, String tableName) {
        String sql = readSqlFile(path);
        return sql.replaceAll("\\{tableName\\}", tableName);
    }

    protected void createTable(String sql) throws JdbcException {
        try {
            getSqlTemplate().createTable(sql);
        } catch (Exception e) {
            throw new JdbcException("Create table error, sql=" + sql, e);
        }
    }

    protected void createOracleTable(String sql, String tablename) throws JdbcException {
        Set<String> noSeqs= new HashSet<String>(Arrays.asList("lts_node", "lts_node_group_store"));

        try {
            if(!noSeqs.contains(tablename)){
                //建立sequence
                getSqlTemplate().createOracleTable("CREATE SEQUENCE seq" + tablename
                        + " START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");
                LOGGER.info("sequence seq" + tablename + " is created.");
            }

            //建立表
            getSqlTemplate().createOracleTable(sql);
            LOGGER.info("table  " + tablename + " is created.");


            if(!noSeqs.contains(tablename)){
                //建立触发器， 插入时插入自增字段
                getSqlTemplate().createOracleTable("CREATE OR REPLACE TRIGGER tg" + tablename
                        + " BEFORE INSERT ON \"" + tablename
                        + "\" REFERENCING OLD AS \"OLD\" NEW AS \"NEW\" FOR EACH ROW ENABLE WHEN (new.\"id\" is null)"
                        + "  begin select seq" + tablename + ".nextval into:new.\"id\" from dual;   end;");
                LOGGER.info("table  " + tablename + "'s trigger is created.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new JdbcException("Create table error, sql=" + sql, e);
        }
    }

}
