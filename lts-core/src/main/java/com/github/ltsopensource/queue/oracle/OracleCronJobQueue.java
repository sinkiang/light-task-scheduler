package com.github.ltsopensource.queue.oracle;

import com.github.ltsopensource.admin.request.JobQueueReq;
import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.core.support.JobQueueUtils;
import com.github.ltsopensource.queue.CronJobQueue;
import com.github.ltsopensource.queue.domain.JobPo;
import com.github.ltsopensource.queue.mysql.support.RshHolder;
import com.github.ltsopensource.store.jdbc.builder.DeleteSql;
import com.github.ltsopensource.store.jdbc.builder.Delim;
import com.github.ltsopensource.store.jdbc.builder.SelectSql;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleCronJobQueue extends OracleSchedulerJobQueue implements CronJobQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleCronJobQueue.class);

    public OracleCronJobQueue(Config config) {
        super(config);
        if(!OracleUtils.tableExists(getTableName(), getSqlTemplate())){

            createOracleTable(readSqlFile("sql/oracle/lts_cron_job_queue.sql", getTableName()), getTableName());
        }
    }

    @Override
    protected String getTableName(JobQueueReq request) {
        return getTableName();
    }

    @Override
    public boolean add(JobPo jobPo) {
        LOGGER.debug("[zjj] add, jobPo:{} ", jobPo);

        return super.add(getTableName(), jobPo);
    }

    @Override
    public JobPo getJob(String jobId) {
        LOGGER.debug("[zjj] getJob, jobId:{} ", jobId);
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
        LOGGER.debug("[zjj] remove, jobId:{} ", jobId);
        return new DeleteSql(getSqlTemplate())
                .delete()
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"job_id\" = ?", jobId)
                .doDelete() == 1;
    }

    @Override
    public JobPo getJob(String taskTrackerNodeGroup, String taskId) {
        LOGGER.debug("[zjj] getJob, taskId:{} ", taskId);

        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"task_id\" = ?", taskId)
                .and("\"task_tracker_node_group\" = ?", taskTrackerNodeGroup)
                .single(RshHolder.JOB_PO_RSH);
    }

    protected String getTableName() {
        return JobQueueUtils.CRON_JOB_QUEUE;
    }
}
