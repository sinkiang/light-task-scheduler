package com.github.ltsopensource.monitor.access.oracle;

import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.commons.utils.CollectionUtils;
import com.github.ltsopensource.monitor.access.domain.TaskTrackerMDataPo;
import com.github.ltsopensource.monitor.access.face.TaskTrackerMAccess;
import com.github.ltsopensource.store.jdbc.builder.Delim;
import com.github.ltsopensource.store.jdbc.builder.InsertSql;

import java.util.List;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleTaskTrackerMAccess extends OracleAbstractJdbcAccess implements TaskTrackerMAccess {
    public OracleTaskTrackerMAccess(Config config) {
        super(config);
    }

    @Override
    protected String getTableName() {
        return "lts_admin_tt_monitor_data";
    }

    @Override
    public void insert(List<TaskTrackerMDataPo> taskTrackerMDataPos) {

        if (CollectionUtils.isEmpty(taskTrackerMDataPos)) {
            return;
        }

        InsertSql insertSql = new InsertSql(getSqlTemplate())
                .insert(Delim.ORACLE, getTableName())
                .columns(Delim.ORACLE, "gmt_created",
                        "node_group",
                        "identity",
                        "timestamp",
                        "exe_success_num",
                        "exe_failed_num",
                        "exe_later_num",
                        "exe_exception_num",
                        "total_running_time");

        for (TaskTrackerMDataPo taskTrackerMDataPo : taskTrackerMDataPos) {
            insertSql.values(
                    taskTrackerMDataPo.getGmtCreated(),
                    taskTrackerMDataPo.getNodeGroup(),
                    taskTrackerMDataPo.getIdentity(),
                    taskTrackerMDataPo.getTimestamp(),
                    taskTrackerMDataPo.getExeSuccessNum(),
                    taskTrackerMDataPo.getExeFailedNum(),
                    taskTrackerMDataPo.getExeLaterNum(),
                    taskTrackerMDataPo.getExeExceptionNum(),
                    taskTrackerMDataPo.getTotalRunningTime()
            );
        }

        insertSql.doBatchInsert();

    }
}
