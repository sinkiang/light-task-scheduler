package com.github.ltsopensource.queue.oracle;

import com.github.ltsopensource.admin.request.JobQueueReq;
import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.core.support.JobQueueUtils;
import com.github.ltsopensource.queue.ExecutingJobQueue;
import com.github.ltsopensource.queue.domain.JobPo;
import com.github.ltsopensource.queue.mysql.support.RshHolder;
import com.github.ltsopensource.store.jdbc.builder.DeleteSql;
import com.github.ltsopensource.store.jdbc.builder.Delim;
import com.github.ltsopensource.store.jdbc.builder.SelectSql;

import java.util.List;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleExecutingJobQueue extends AbstractOracleJobQueue implements ExecutingJobQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleExecutingJobQueue.class);

    public OracleExecutingJobQueue(Config config) {
        super(config);
        if(!OracleUtils.tableExists(getTableName(), getSqlTemplate())){
            // create table
            createOracleTable(readSqlFile("sql/oracle/lts_executing_job_queue.sql", getTableName()), getTableName());
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
    public List<JobPo> getJobs(String taskTrackerIdentity) {
        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"task_tracker_identity\" = ?", taskTrackerIdentity)
                .list(RshHolder.JOB_PO_LIST_RSH);
    }

    @Override
    public List<JobPo> getDeadJobs(long deadline) {
        LOGGER.debug("[zjj] getDeadJobs, deadline:{}, tablename:{} ", deadline, getTableName());
        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"gmt_modified\" < ?", deadline)
                .list(RshHolder.JOB_PO_LIST_RSH);
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

    private String getTableName() {
        return JobQueueUtils.EXECUTING_JOB_QUEUE;
    }
}
