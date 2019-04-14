package com.github.ltsopensource.queue.oracle;

import com.github.ltsopensource.admin.request.JobQueueReq;
import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.core.support.JobQueueUtils;
import com.github.ltsopensource.queue.SuspendJobQueue;
import com.github.ltsopensource.queue.domain.JobPo;
import com.github.ltsopensource.queue.mysql.support.RshHolder;
import com.github.ltsopensource.store.jdbc.builder.DeleteSql;
import com.github.ltsopensource.store.jdbc.builder.Delim;
import com.github.ltsopensource.store.jdbc.builder.SelectSql;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleSuspendJobQueue extends AbstractOracleJobQueue implements SuspendJobQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleSuspendJobQueue.class);

    public OracleSuspendJobQueue(Config config) {
        super(config);
        if(!OracleUtils.tableExists(getTableName(), getSqlTemplate())){
            createOracleTable(readSqlFile("sql/oracle/lts_suspend_job_queue.sql", getTableName()), getTableName());
        }
    }

    @Override
    protected String getTableName(JobQueueReq request) {
        return getTableName();
    }

    @Override
    public boolean add(JobPo jobPo) {
        LOGGER.info("add, jobPo:{}" , jobPo);

        return add(getTableName(), jobPo);
    }

    @Override
    public JobPo getJob(String jobId) {
        LOGGER.info("getJob, jobId:{}" , jobId);
        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"job_id\" = ?", jobId)
                .single(RshHolder.JOB_PO_RSH);
    }

    @Override
    public boolean remove(String jobId) {
        LOGGER.info("remove, jobId:{}" , jobId);

        return new DeleteSql(getSqlTemplate())
                .delete()
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"job_id\" = ?", jobId)
                .doDelete() == 1;
    }

    @Override
    public JobPo getJob(String taskTrackerNodeGroup, String taskId) {
        LOGGER.info("getJob, taskId:{}" , taskId);
        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"task_id\" = ?", taskId)
                .and("\"task_tracker_node_group\" = ?", taskTrackerNodeGroup)
                .single(RshHolder.JOB_PO_RSH);
    }

    private String getTableName() {
        return JobQueueUtils.SUSPEND_JOB_QUEUE;
    }
}
