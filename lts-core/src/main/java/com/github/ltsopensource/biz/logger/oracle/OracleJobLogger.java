package com.github.ltsopensource.biz.logger.oracle;

import com.github.ltsopensource.admin.response.PaginationRsp;
import com.github.ltsopensource.biz.logger.JobLogger;
import com.github.ltsopensource.biz.logger.domain.JobLogPo;
import com.github.ltsopensource.biz.logger.domain.JobLoggerRequest;
import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.commons.utils.CollectionUtils;
import com.github.ltsopensource.core.json.JSON;
import com.github.ltsopensource.queue.mysql.support.RshHolder;
import com.github.ltsopensource.queue.oracle.OracleUtils;
import com.github.ltsopensource.store.jdbc.JdbcAbstractAccess;
import com.github.ltsopensource.store.jdbc.builder.*;
import com.github.ltsopensource.store.jdbc.dbutils.JdbcTypeUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * job日志oracle操作类
 * Created by zhangjianjun on 2017/5/22.
 */
public class OracleJobLogger extends JdbcAbstractAccess implements JobLogger {
    public OracleJobLogger(Config config) {
        super(config);
        if(!OracleUtils.tableExists(getTableName(), getSqlTemplate())){
            createOracleTable(readSqlFile("sql/oracle/lts_job_log_po.sql"), getTableName());
        }
    }

    @Override
    public void log(JobLogPo jobLogPo) {
        if (jobLogPo == null) {
            return;
        }
        InsertSql insertSql = buildInsertSql();

        setInsertSqlValues(insertSql, jobLogPo).doInsert();

    }

    @Override
    public void log(List<JobLogPo> jobLogPos) {
        if (CollectionUtils.isEmpty(jobLogPos)) {
            return;
        }

        InsertSql insertSql = buildInsertSql();

        for (JobLogPo jobLogPo : jobLogPos) {
            setInsertSqlValues(insertSql, jobLogPo);
        }
        insertSql.doBatchInsert();

    }

    @Override
    public PaginationRsp<JobLogPo> search(JobLoggerRequest request) {
        PaginationRsp<JobLogPo> response = new PaginationRsp<JobLogPo>();

        BigDecimal bigDecimal = new SelectSql(getSqlTemplate())
                .select()
                .columns("count(1)")
                .from()
                .table(Delim.ORACLE, getTableName())
                .whereSql(buildWhereSql(request))
                .single();

        Long results = bigDecimal.longValue();
        response.setResults(results.intValue());
        if (results == 0) {
            return response;
        }
        // 查询 rows
        List<JobLogPo> rows = new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName())
                .whereSql(buildWhereSql(request))
                .orderBy()
                .column(Delim.ORACLE, "log_time", OrderByType.DESC)
                .limitOracle(request.getStart(), request.getLimit())
                .list(RshHolder.JOB_LOGGER_LIST_RSH);
        response.setRows(rows);

        return response;
    }

    private InsertSql buildInsertSql() {
        return new InsertSql(getSqlTemplate())
                .insert(Delim.ORACLE, getTableName())
                .columns(Delim.ORACLE, "log_time",
                        "gmt_created",
                        "log_type",
                        "success",
                        "msg",
                        "task_tracker_identity",
                        "level",
                        "task_id",
                        "real_task_id",
                        "job_id",
                        "job_type",
                        "priority",
                        "submit_node_group",
                        "task_tracker_node_group",
                        "ext_params",
                        "internal_ext_params",
                        "need_feedback",
                        "cron_expression",
                        "trigger_time",
                        "retry_times",
                        "max_retry_times",
                        "rely_on_prev_cycle",
                        "repeat_count",
                        "repeated_count",
                        "repeat_interval"
                );
    }

    private String getTableName() {
        return "lts_job_log_po";
    }

    private InsertSql setInsertSqlValues(InsertSql insertSql, JobLogPo jobLogPo) {
        return insertSql.values(jobLogPo.getLogTime(),
                jobLogPo.getGmtCreated(),
                jobLogPo.getLogType().name(),
                jobLogPo.isSuccess(),
                jobLogPo.getMsg(),
                jobLogPo.getTaskTrackerIdentity(),
                jobLogPo.getLevel().name(),
                jobLogPo.getTaskId(),
                jobLogPo.getRealTaskId(),
                jobLogPo.getJobId(),
                jobLogPo.getJobType() == null ? null : jobLogPo.getJobType().name(),
                jobLogPo.getPriority(),
                jobLogPo.getSubmitNodeGroup(),
                jobLogPo.getTaskTrackerNodeGroup(),
                JSON.toJSONString(jobLogPo.getExtParams()),
                JSON.toJSONString(jobLogPo.getInternalExtParams()),
                jobLogPo.isNeedFeedback(),
                jobLogPo.getCronExpression(),
                jobLogPo.getTriggerTime(),
                jobLogPo.getRetryTimes(),
                jobLogPo.getMaxRetryTimes(),
                jobLogPo.getDepPreCycle(),
                jobLogPo.getRepeatCount(),
                jobLogPo.getRepeatedCount(),
                jobLogPo.getRepeatInterval());
    }

    private WhereSql buildWhereSql(JobLoggerRequest request) {
        return new WhereSql()
                .andOnNotEmpty("\"task_id\" = ?", request.getTaskId())
                .andOnNotEmpty("\"real_task_id\" = ?", request.getRealTaskId())
                .andOnNotEmpty("\"task_tracker_node_group\" = ?", request.getTaskTrackerNodeGroup())
                .andBetween(Delim.ORACLE, "log_time", JdbcTypeUtils.toTimestamp(request.getStartLogTime()), JdbcTypeUtils.toTimestamp(request.getEndLogTime()))
                ;
    }

}
