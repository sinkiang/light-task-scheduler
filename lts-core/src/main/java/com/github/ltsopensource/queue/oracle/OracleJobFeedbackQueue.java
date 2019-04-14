package com.github.ltsopensource.queue.oracle;

import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.commons.utils.CollectionUtils;
import com.github.ltsopensource.core.json.JSON;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.core.support.JobQueueUtils;
import com.github.ltsopensource.queue.JobFeedbackQueue;
import com.github.ltsopensource.queue.domain.JobFeedbackPo;
import com.github.ltsopensource.queue.mysql.support.RshHolder;
import com.github.ltsopensource.store.jdbc.JdbcAbstractAccess;
import com.github.ltsopensource.store.jdbc.builder.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleJobFeedbackQueue extends JdbcAbstractAccess implements JobFeedbackQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleJobFeedbackQueue.class);

    public OracleJobFeedbackQueue(Config config) {
        super(config);
    }

    @Override
    public boolean createQueue(String jobClientNodeGroup) {
        if(!OracleUtils.tableExists(getTableName(jobClientNodeGroup), getSqlTemplate())){
            createOracleTable(readSqlFile("sql/oracle/lts_job_feedback_queue.sql", getTableName(jobClientNodeGroup)), getTableName(jobClientNodeGroup));
        }
        return true;
    }

    @Override
    public boolean removeQueue(String jobClientNodeGroup) {
        return new DropTableSql(getSqlTemplate())
                .drop(JobQueueUtils.getFeedbackQueueName(jobClientNodeGroup))
                .doDrop();
    }

    private String getTableName(String jobClientNodeGroup) {
        return JobQueueUtils.getFeedbackQueueName(jobClientNodeGroup);
    }

    @Override
    public boolean add(List<JobFeedbackPo> jobFeedbackPos) {
        LOGGER.debug("[zjj] add, jobFeedbackPos:{} ", JSON.toJSONString(jobFeedbackPos));

        if (CollectionUtils.isEmpty(jobFeedbackPos)) {
            return true;
        }
        // insert ignore duplicate record
        for (JobFeedbackPo jobFeedbackPo : jobFeedbackPos) {
            String jobClientNodeGroup = jobFeedbackPo.getJobRunResult().getJobMeta().getJob().getSubmitNodeGroup();
            new InsertSql(getSqlTemplate())
                    .insert(Delim.ORACLE, getTableName(jobClientNodeGroup))
                    .columns(Delim.ORACLE, "gmt_created", "job_result")
                    .values(jobFeedbackPo.getGmtCreated(), JSON.toJSONString(jobFeedbackPo.getJobRunResult()))
                    .doInsert();
        }
        return true;
    }

    @Override
    public boolean remove(String jobClientNodeGroup, String id) {
        LOGGER.debug("[zjj] remove, id:{}, tablename:{} ", id, getTableName(jobClientNodeGroup));
        return new DeleteSql(getSqlTemplate())
                .delete()
                .from()
                .table(Delim.ORACLE, getTableName(jobClientNodeGroup))
                .where("\"id\" = ?", id)
                .doDelete() == 1;
    }

    @Override
    public long getCount(String jobClientNodeGroup) {
        return ((BigDecimal) new SelectSql(getSqlTemplate())
                .select()
                .columns("count(1)")
                .from()
                .table(Delim.ORACLE, getTableName(jobClientNodeGroup))
                .single()).intValue();
    }

    @Override
    public List<JobFeedbackPo> fetchTop(String jobClientNodeGroup, int top) {

        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName(jobClientNodeGroup))
                .orderBy()
                .column(Delim.ORACLE, "gmt_created", OrderByType.ASC)
                .limitOracle(0, top)
                .list(RshHolder.JOB_FEED_BACK_LIST_RSH);
    }
}
