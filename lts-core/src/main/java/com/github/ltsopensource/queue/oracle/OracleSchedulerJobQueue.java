package com.github.ltsopensource.queue.oracle;

import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.support.SystemClock;
import com.github.ltsopensource.queue.SchedulerJobQueue;
import com.github.ltsopensource.queue.domain.JobPo;
import com.github.ltsopensource.queue.mysql.support.RshHolder;
import com.github.ltsopensource.store.jdbc.builder.Delim;
import com.github.ltsopensource.store.jdbc.builder.SelectSql;
import com.github.ltsopensource.store.jdbc.builder.UpdateSql;

import java.util.List;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public abstract class OracleSchedulerJobQueue extends AbstractOracleJobQueue implements SchedulerJobQueue {
    public OracleSchedulerJobQueue(Config config) {
        super(config);
    }

    @Override
    public boolean updateLastGenerateTriggerTime(String jobId, Long lastGenerateTriggerTime) {
        return new UpdateSql(getSqlTemplate())
                .update()
                .table(Delim.ORACLE, getTableName())
                .set(Delim.ORACLE, "last_generate_trigger_time", lastGenerateTriggerTime)
                .set(Delim.ORACLE, "gmt_modified", SystemClock.now())
                .where("\"job_id\" = ? ", jobId)
                .doUpdate() == 1;
    }

    @Override
    public List<JobPo> getNeedGenerateJobPos(Long checkTime, int topSize) {
        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"rely_on_prev_cycle\" = ?", false)
                .and("\"last_generate_trigger_time\" <= ?", checkTime)
                .limitOracle(0, topSize)
                .list(RshHolder.JOB_PO_LIST_RSH);
    }

    protected abstract String getTableName();
}
