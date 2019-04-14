package com.github.ltsopensource.monitor.access.oracle;

import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.monitor.access.MonitorAccessFactory;
import com.github.ltsopensource.monitor.access.face.*;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleMonitorAccessFactory implements MonitorAccessFactory {
    @Override
    public JobTrackerMAccess getJobTrackerMAccess(Config config) {
        return new OracleJobTrackerMAccess(config);
    }

    @Override
    public TaskTrackerMAccess getTaskTrackerMAccess(Config config) {
        return new OracleTaskTrackerMAccess(config);
    }

    @Override
    public JVMGCAccess getJVMGCAccess(Config config) {
        return new OracleJVMGCAccess(config);
    }

    @Override
    public JVMMemoryAccess getJVMMemoryAccess(Config config) {
        return new OracleJVMMemoryAccess(config);
    }

    @Override
    public JVMThreadAccess getJVMThreadAccess(Config config) {
        return new OracleJVMThreadAccess(config);
    }

    @Override
    public JobClientMAccess getJobClientMAccess(Config config) {
        return new OracleJobClientMAccess(config);
    }
}
