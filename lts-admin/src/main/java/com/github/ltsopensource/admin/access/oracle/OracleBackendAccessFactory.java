package com.github.ltsopensource.admin.access.oracle;

import com.github.ltsopensource.admin.access.BackendAccessFactory;
import com.github.ltsopensource.admin.access.face.*;
import com.github.ltsopensource.core.cluster.Config;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleBackendAccessFactory implements BackendAccessFactory {
    @Override
    public BackendJobTrackerMAccess getJobTrackerMAccess(Config config) {
        return new OracleBackendJobTrackerMAccess(config);
    }

    @Override
    public BackendJobClientMAccess getBackendJobClientMAccess(Config config) {
        return new OracleBackendJobClientMAccess(config);
    }

    @Override
    public BackendJVMGCAccess getBackendJVMGCAccess(Config config) {
        return new OracleBackendJVMGCAccess(config);
    }

    @Override
    public BackendJVMMemoryAccess getBackendJVMMemoryAccess(Config config) {
        return new OracleBackendJVMMemoryAccess(config);
    }

    @Override
    public BackendJVMThreadAccess getBackendJVMThreadAccess(Config config) {
        return new OracleBackendJVMThreadAccess(config);
    }

    @Override
    public BackendNodeOnOfflineLogAccess getBackendNodeOnOfflineLogAccess(Config config) {
        return new OracleBackendNodeOnOfflineLogAccess(config);
    }

    @Override
    public BackendTaskTrackerMAccess getBackendTaskTrackerMAccess(Config config) {
        return new OracleBackendTaskTrackerMAccess(config);
    }

    @Override
    public BackendNodeAccess getBackendNodeAccess(Config config) {
        return new OracleBackendNodeAccess(config);
    }
}
