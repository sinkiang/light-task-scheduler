package com.github.ltsopensource.monitor.access.oracle;

import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.commons.utils.CollectionUtils;
import com.github.ltsopensource.monitor.access.domain.JVMGCDataPo;
import com.github.ltsopensource.monitor.access.face.JVMGCAccess;
import com.github.ltsopensource.store.jdbc.builder.Delim;
import com.github.ltsopensource.store.jdbc.builder.InsertSql;

import java.util.List;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleJVMGCAccess extends OracleAbstractJdbcAccess implements JVMGCAccess {
    public OracleJVMGCAccess(Config config) {
        super(config);
    }

    @Override
    protected String getTableName() {
        return "lts_admin_jvm_gc";    }

    @Override
    public void insert(List<JVMGCDataPo> jvmGCDataPos) {
        if (CollectionUtils.isEmpty(jvmGCDataPos)) {
            return;
        }

        InsertSql insertSql = new InsertSql(getSqlTemplate())
                .insert(Delim.ORACLE, getTableName())
                .columns(Delim.ORACLE, "gmt_created",
                        "identity",
                        "timestamp",
                        "node_type",
                        "node_group",
                        "young_gc_collection_count",
                        "young_gc_collection_time",
                        "full_gc_collection_count",
                        "full_gc_collection_time",
                        "span_young_gc_collection_count",
                        "span_young_gc_collection_time",
                        "span_full_gc_collection_count",
                        "span_full_gc_collection_time");

        for (JVMGCDataPo po : jvmGCDataPos) {
            insertSql.values(
                    po.getGmtCreated(),
                    po.getIdentity(),
                    po.getTimestamp(),
                    po.getNodeType().name(),
                    po.getNodeGroup(),
                    po.getYoungGCCollectionCount(),
                    po.getYoungGCCollectionTime(),
                    po.getFullGCCollectionCount(),
                    po.getFullGCCollectionTime(),
                    po.getSpanYoungGCCollectionCount(),
                    po.getSpanYoungGCCollectionTime(),
                    po.getSpanFullGCCollectionCount(),
                    po.getSpanFullGCCollectionTime()
            );
        }

        insertSql.doBatchInsert();
    }
}
