package com.github.ltsopensource.queue.oracle;

import com.github.ltsopensource.core.AppContext;
import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.queue.*;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleJobQueueFactory implements JobQueueFactory {
    @Override
    public CronJobQueue getCronJobQueue(Config config) {
        return new OracleCronJobQueue(config);
    }

    @Override
    public RepeatJobQueue getRepeatJobQueue(Config config) {
        return new OracleRepeatJobQueue(config);
    }

    @Override
    public ExecutableJobQueue getExecutableJobQueue(Config config) {
        return new OracleExecutableJobQueue(config);
    }

    @Override
    public ExecutingJobQueue getExecutingJobQueue(Config config) {
        return new OracleExecutingJobQueue(config);
    }

    @Override
    public JobFeedbackQueue getJobFeedbackQueue(Config config) {
        return new OracleJobFeedbackQueue(config);
    }

    @Override
    public NodeGroupStore getNodeGroupStore(Config config) {
        return new OracleNodeGroupStore(config);
    }

    @Override
    public SuspendJobQueue getSuspendJobQueue(Config config) {
        return new OracleSuspendJobQueue(config);
    }

    @Override
    public PreLoader getPreLoader(AppContext appContext) {
        return new OraclePreLoader(appContext);
    }
}
