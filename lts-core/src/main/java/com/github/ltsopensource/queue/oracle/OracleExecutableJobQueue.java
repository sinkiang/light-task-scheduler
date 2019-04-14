package com.github.ltsopensource.queue.oracle;

import com.github.ltsopensource.admin.request.JobQueueReq;
import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.commons.utils.StringUtils;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.core.support.JobQueueUtils;
import com.github.ltsopensource.core.support.SystemClock;
import com.github.ltsopensource.queue.ExecutableJobQueue;
import com.github.ltsopensource.queue.domain.JobPo;
import com.github.ltsopensource.queue.mysql.support.RshHolder;
import com.github.ltsopensource.store.jdbc.builder.*;
import com.github.ltsopensource.store.jdbc.exception.TableNotExistException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleExecutableJobQueue extends AbstractOracleJobQueue implements ExecutableJobQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleExecutableJobQueue.class);

    public OracleExecutableJobQueue(Config config) {
        super(config);
    }

    @Override
    protected String getTableName(JobQueueReq request) {
        if (StringUtils.isEmpty(request.getTaskTrackerNodeGroup())) {
            throw new IllegalArgumentException(" takeTrackerNodeGroup cat not be null");
        }
        return getTableName(request.getTaskTrackerNodeGroup());
    }

    @Override
    public boolean createQueue(String taskTrackerNodeGroup) {
        if(!OracleUtils.tableExists(getTableName(taskTrackerNodeGroup), getSqlTemplate())){
            createOracleTable(readSqlFile("sql/oracle/lts_executable_job_queue.sql", getTableName(taskTrackerNodeGroup)), getTableName(taskTrackerNodeGroup));
        }
        return true;
    }

    @Override
    public boolean removeQueue(String taskTrackerNodeGroup) {
        return new DropTableSql(getSqlTemplate())
                .drop(JobQueueUtils.getExecutableQueueName(taskTrackerNodeGroup))
                .doDrop();
    }

    private String getTableName(String taskTrackerNodeGroup) {
        return JobQueueUtils.getExecutableQueueName(taskTrackerNodeGroup);
    }

    @Override
    public boolean add(JobPo jobPo) {
        LOGGER.debug("[zjj] add method, jobPo:{}", jobPo);
        try {
            jobPo.setGmtModified(SystemClock.now());
            return super.add(getTableName(jobPo.getTaskTrackerNodeGroup()), jobPo);
        } catch (TableNotExistException e) {
            // 表不存在
            createQueue(jobPo.getTaskTrackerNodeGroup());
            add(jobPo);
        }
        return true;
    }

    @Override
    public boolean remove(String taskTrackerNodeGroup, String jobId) {
        LOGGER.debug("[zjj] remove method, jobId:{}", jobId);
        return new DeleteSql(getSqlTemplate())
                .delete()
                .from()
                .table(Delim.ORACLE, getTableName(taskTrackerNodeGroup))
                .where("\"job_id\" = ?", jobId)
                .doDelete() == 1;
    }

    @Override
    public long countJob(String realTaskId, String taskTrackerNodeGroup) {
        LOGGER.debug("[zjj] countJob method, realTaskId:{}", realTaskId);

        return ((BigDecimal) new SelectSql(getSqlTemplate())
                .select()
                .columns("COUNT(1)")
                .from()
                .table(Delim.ORACLE, getTableName(taskTrackerNodeGroup))
                .where("\"real_task_id\" = ?", realTaskId)
                .and("\"task_tracker_node_group\" = ?", taskTrackerNodeGroup)
                .single()).longValue();
    }

    @Override
    public boolean removeBatch(String realTaskId, String taskTrackerNodeGroup) {
        LOGGER.debug("[zjj] removeBatch method, realTaskId:{}", realTaskId);

        new DeleteSql(getSqlTemplate())
                .delete()
                .from()
                .table(Delim.ORACLE, getTableName(taskTrackerNodeGroup))
                .where("\"real_task_id\" = ?", realTaskId)
                .and("\"task_tracker_node_group\" = ?", taskTrackerNodeGroup)
                .doDelete();
        return true;
    }

    @Override
    public void resume(JobPo jobPo) {
        LOGGER.debug("[zjj] resume method, jobPo:{}", jobPo);

        new UpdateSql(getSqlTemplate())
                .update()
                .table(Delim.ORACLE, getTableName(jobPo.getTaskTrackerNodeGroup()))
                .set(Delim.ORACLE, "is_running", false)
                .set(Delim.ORACLE, "task_tracker_identity", null)
                .set(Delim.ORACLE, "gmt_modified", SystemClock.now())
                .where("job_id=?", jobPo.getJobId())
                .doUpdate();
    }

    @Override
    public List<JobPo> getDeadJob(String taskTrackerNodeGroup, long deadline) {
        LOGGER.debug("[zjj] getDeadJob method, deadline:{}", deadline);

        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName(taskTrackerNodeGroup))
                .where("\"is_running\" = ?", true)
                .and("\"gmt_modified\" < ?", deadline)
                .list(RshHolder.JOB_PO_LIST_RSH);
    }

    @Override
    public JobPo getJob(String taskTrackerNodeGroup, String taskId) {
        LOGGER.debug("[zjj] getJob method, taskId:{}", taskId);

        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName(taskTrackerNodeGroup))
                .where("\"task_id\" = ?", taskId)
                .and("\"task_tracker_node_group\" = ?", taskTrackerNodeGroup)
                .single(RshHolder.JOB_PO_RSH);
    }
}
