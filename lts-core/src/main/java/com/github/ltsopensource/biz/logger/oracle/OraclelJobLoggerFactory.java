package com.github.ltsopensource.biz.logger.oracle;

import com.github.ltsopensource.biz.logger.JobLogger;
import com.github.ltsopensource.biz.logger.JobLoggerFactory;
import com.github.ltsopensource.core.cluster.Config;

/**
 * Created by zhangjianjun on 2017/5/22.
 */
public class OraclelJobLoggerFactory implements JobLoggerFactory {
    @Override
    public JobLogger getJobLogger(Config config) {
        return new OracleJobLogger(config);
    }
}
