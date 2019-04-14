package com.github.ltsopensource.monitor.access.oracle;

import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.commons.utils.CollectionUtils;
import com.github.ltsopensource.monitor.access.domain.JobClientMDataPo;
import com.github.ltsopensource.monitor.access.face.JobClientMAccess;
import com.github.ltsopensource.store.jdbc.builder.Delim;
import com.github.ltsopensource.store.jdbc.builder.InsertSql;

import java.util.List;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleJobClientMAccess extends OracleAbstractJdbcAccess implements JobClientMAccess {
    public OracleJobClientMAccess(Config config) {
        super(config);
    }

    @Override
    protected String getTableName() {
        return "lts_admin_jc_monitor_data";
    }

    @Override
    public void insert(List<JobClientMDataPo> jobClientMDataPos) {
        if (CollectionUtils.isEmpty(jobClientMDataPos)) {
            return;
        }
        InsertSql insertSql = new InsertSql(getSqlTemplate())
                .insert(Delim.ORACLE, getTableName())
                .columns(Delim.ORACLE, "gmt_created",
                        "node_group",
                        "identity",
                        "timestamp",
                        "submit_success_num",
                        "submit_failed_num",
                        "fail_store_num",
                        "submit_fail_store_num",
                        "handle_feedback_num");

        for (JobClientMDataPo jobClientMDataPo : jobClientMDataPos) {
            insertSql.values(
                    jobClientMDataPo.getGmtCreated(),
                    jobClientMDataPo.getNodeGroup(),
                    jobClientMDataPo.getIdentity(),
                    jobClientMDataPo.getTimestamp(),
                    jobClientMDataPo.getSubmitSuccessNum(),
                    jobClientMDataPo.getSubmitFailedNum(),
                    jobClientMDataPo.getFailStoreNum(),
                    jobClientMDataPo.getSubmitFailStoreNum(),
                    jobClientMDataPo.getHandleFeedbackNum()
            );
        }
        insertSql.doBatchInsert();

    }
}
