package com.github.ltsopensource.admin.access.oracle;

import com.github.ltsopensource.admin.access.RshHandler;
import com.github.ltsopensource.admin.access.domain.NodeOnOfflineLog;
import com.github.ltsopensource.admin.access.face.BackendNodeOnOfflineLogAccess;
import com.github.ltsopensource.admin.request.NodeOnOfflineLogPaginationReq;
import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.monitor.access.oracle.OracleAbstractJdbcAccess;
import com.github.ltsopensource.store.jdbc.builder.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleBackendNodeOnOfflineLogAccess extends OracleAbstractJdbcAccess implements BackendNodeOnOfflineLogAccess {
    public OracleBackendNodeOnOfflineLogAccess(Config config) {
        super(config);
    }

    @Override
    protected String getTableName() {
        return "lts_admin_noo_log";
    }

    @Override
    public void insert(List<NodeOnOfflineLog> nodeOnOfflineLogs) {
        InsertSql insertSql = new InsertSql(getSqlTemplate())
                .insert(Delim.ORACLE, getTableName())
                .columns(Delim.ORACLE, "log_time",
                        "event",
                        "node_type",
                        "cluster_name",
                        "ip",
                        "port",
                        "host_name",
                        "group",
                        "create_time",
                        "threads",
                        "identity",
                        "http_cmd_port");
        for (NodeOnOfflineLog nodeOnOfflineLog : nodeOnOfflineLogs) {
            insertSql.values(nodeOnOfflineLog.getLogTime().getTime(),
                    nodeOnOfflineLog.getEvent(),
                    nodeOnOfflineLog.getNodeType().name(),
                    nodeOnOfflineLog.getClusterName(),
                    nodeOnOfflineLog.getIp(),
                    nodeOnOfflineLog.getPort(),
                    nodeOnOfflineLog.getHostName(),
                    nodeOnOfflineLog.getGroup(),
                    nodeOnOfflineLog.getCreateTime(),
                    nodeOnOfflineLog.getThreads(),
                    nodeOnOfflineLog.getIdentity(),
                    nodeOnOfflineLog.getHttpCmdPort()
            );
        }
        insertSql.doBatchInsert();
    }

    @Override
    public List<NodeOnOfflineLog> select(NodeOnOfflineLogPaginationReq request) {
        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName())
                .whereSql(buildWhereSql(request))
                .orderBy()
                .column(Delim.ORACLE, "log_time", OrderByType.DESC)
                .limitOracle(request.getStart(), request.getLimit())
                .list(RshHandler.NODE_ON_OFFLINE_LOG_LIST_RSH);
    }

    @Override
    public Long count(NodeOnOfflineLogPaginationReq request) {
        BigDecimal single = new SelectSql(getSqlTemplate())
                .select()
                .columns("count(1)")
                .from()
                .table(Delim.ORACLE, getTableName())
                .whereSql(buildWhereSql(request))
                .single();

        return single.longValue();
    }

    @Override
    public void delete(NodeOnOfflineLogPaginationReq request) {
        new DeleteSql(getSqlTemplate())
                .delete()
                .from()
                .table(Delim.ORACLE, getTableName())
                .whereSql(buildWhereSql(request))
                .doDelete();
    }

    private WhereSql buildWhereSql(NodeOnOfflineLogPaginationReq request){
        return new WhereSql()
                .andOnNotEmpty("\"identity\" = ?", request.getIdentity())
                .andOnNotEmpty("\"group\" = ?", request.getGroup())
                .andOnNotEmpty("\"event\" = ?", request.getEvent())
                .andBetween(Delim.ORACLE, "log_time", request.getStartLogTime().getTime(), request.getEndLogTime().getTime());
    }
}
