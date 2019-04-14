package com.github.ltsopensource.queue.oracle;

import com.github.ltsopensource.core.json.JSON;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.store.jdbc.SqlTemplate;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * oracle工具类
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleUtils.class);
    /**
     * 判断oracle表是否存在
     * @param tableName 表名
     * @return 存在返回true
     */
    public static boolean tableExists(String tableName, SqlTemplate sqlTemplate) {
        BigDecimal num = null;
        try {
            num = sqlTemplate.queryForValue("select count(*)  from user_tables where table_name = ?", tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOGGER.info("tablename:"+tableName+", num:"+ JSON.toJSONString(num));
        return num != null && num.intValue() > 0;
    }

}
