package com.github.ltsopensource.queue.oracle;

import com.github.ltsopensource.admin.response.PaginationRsp;
import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.core.cluster.NodeType;
import com.github.ltsopensource.core.domain.NodeGroupGetReq;
import com.github.ltsopensource.core.support.JobQueueUtils;
import com.github.ltsopensource.core.support.SystemClock;
import com.github.ltsopensource.queue.NodeGroupStore;
import com.github.ltsopensource.queue.domain.NodeGroupPo;
import com.github.ltsopensource.queue.mysql.support.RshHolder;
import com.github.ltsopensource.store.jdbc.JdbcAbstractAccess;
import com.github.ltsopensource.store.jdbc.builder.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleNodeGroupStore extends JdbcAbstractAccess implements NodeGroupStore {
    public OracleNodeGroupStore(Config config) {
        super(config);
        if(!OracleUtils.tableExists(getTableName(), getSqlTemplate())){
            createOracleTable(readSqlFile("sql/oracle/lts_node_group_store.sql", JobQueueUtils.NODE_GROUP_STORE), getTableName());
        }
    }

    @Override
    public void addNodeGroup(NodeType nodeType, String name) {

        Long count =((BigDecimal) new SelectSql(getSqlTemplate())
                .select()
                .columns("count(1)")
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"node_type\" = ?", nodeType.name())
                .and("\"name\" = ?", name)
                .single()).longValue();
        if (count > 0) {
            //  already exist
            return;
        }
        new InsertSql(getSqlTemplate())
                .insert(Delim.ORACLE, getTableName())
                .columns(Delim.ORACLE, "node_type", "name", "gmt_created")
                .values(nodeType.name(), name, SystemClock.now())
                .doInsert();
    }

    @Override
    public void removeNodeGroup(NodeType nodeType, String name) {
        new DeleteSql(getSqlTemplate())
                .delete()
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"node_type\" = ?", nodeType.name())
                .and("\"name\" = ?", name)
                .doDelete();
    }

    @Override
    public List<NodeGroupPo> getNodeGroup(NodeType nodeType) {
        return new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName())
                .where("\"node_type\" = ?", nodeType.name())
                .list(RshHolder.NODE_GROUP_LIST_RSH);
    }

    public PaginationRsp<NodeGroupPo> getNodeGroup(NodeGroupGetReq request) {
        PaginationRsp<NodeGroupPo> response = new PaginationRsp<NodeGroupPo>();

        Long results = ((BigDecimal)new SelectSql(getSqlTemplate())
                .select()
                .columns("count(1)")
                .from()
                .table(Delim.ORACLE, getTableName())
                .whereSql(
                        new WhereSql()
                                .andOnNotNull("\"node_type\" = ?", request.getNodeType() == null ? null : request.getNodeType().name())
                                .andOnNotEmpty("\"name\" = ?", request.getNodeGroup())
                )
                .single()).longValue();
        response.setResults(results.intValue());
        if (results == 0) {
            return response;
        }

        List<NodeGroupPo> rows = new SelectSql(getSqlTemplate())
                .select()
                .all()
                .from()
                .table(Delim.ORACLE, getTableName())
                .whereSql(
                        new WhereSql()
                                .andOnNotNull("\"node_type\" = ?", request.getNodeType() == null ? null : request.getNodeType().name())
                                .andOnNotEmpty("\"name\" = ?", request.getNodeGroup())
                )
                .orderBy()
                .column(Delim.ORACLE, "gmt_created", OrderByType.DESC)
                .limitOracle(request.getStart(), request.getLimit())
                .list(RshHolder.NODE_GROUP_LIST_RSH);

        response.setRows(rows);

        return response;
    }

    private String getTableName() {
        return JobQueueUtils.NODE_GROUP_STORE;
    }
}
