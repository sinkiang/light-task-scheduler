package com.github.ltsopensource.admin.access.oracle;

import com.github.ltsopensource.admin.access.RshHandler;
import com.github.ltsopensource.admin.access.face.BackendJobTrackerMAccess;
import com.github.ltsopensource.admin.request.MDataPaginationReq;
import com.github.ltsopensource.core.cluster.Config;
import com.github.ltsopensource.monitor.access.domain.JobTrackerMDataPo;
import com.github.ltsopensource.monitor.access.oracle.OracleJobTrackerMAccess;
import com.github.ltsopensource.store.jdbc.builder.DeleteSql;
import com.github.ltsopensource.store.jdbc.builder.Delim;
import com.github.ltsopensource.store.jdbc.builder.SelectSql;
import com.github.ltsopensource.store.jdbc.builder.WhereSql;
import com.github.ltsopensource.store.jdbc.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjianjun on 2017/5/23.
 */
public class OracleBackendJobTrackerMAccess extends OracleJobTrackerMAccess implements BackendJobTrackerMAccess {
    public OracleBackendJobTrackerMAccess(Config config) {
        super(config);
    }

    @Override
    public List<JobTrackerMDataPo> querySum(MDataPaginationReq request) {
        return new SelectSql(getSqlTemplate())
                .select()
                .columns("\"timestamp\"",
                        "SUM(\"receive_job_num\") AS receive_job_num",
                        "SUM(\"push_job_num\") AS push_job_num" ,
                        "SUM(\"exe_success_num\") AS exe_success_num" ,
                        "SUM(\"exe_failed_num\") AS exe_failed_num" ,
                        "SUM(\"exe_later_num\") AS exe_later_num" ,
                        "SUM(\"exe_exception_num\") AS exe_exception_num" ,
                        "SUM(\"fix_executing_job_num\") AS fix_executing_job_num")
                .from()
                .table(Delim.ORACLE, getTableName())
                .whereSql(buildWhereSql(request))
                .groupBy(Delim.ORACLE, "timestamp")
                .limitOracle(request.getStart(), request.getLimit())
                .list(RshHandler.JOB_TRACKER_SUM_M_DATA_RSH);
    }

    @Override
    public void delete(MDataPaginationReq request) {
        new DeleteSql(getSqlTemplate())
                .delete()
                .from()
                .table(Delim.ORACLE, getTableName())
                .whereSql(buildWhereSql(request))
                .doDelete();
    }

    @Override
    public List<String> getJobTrackers() {
        return new SelectSql(getSqlTemplate())
                .select()
                .columns("DISTINCT \"identity\" AS identitystr ")
                .from()
                .table(Delim.ORACLE, getTableName())
                .list(new ResultSetHandler<List<String>>() {
                    @Override
                    public List<String> handle(ResultSet rs) throws SQLException {
                        List<String> list = new ArrayList<String>();
                        while (rs.next()) {
                            list.add(rs.getString("identitystr"));
                        }
                        return list;
                    }
                });
    }

    private WhereSql buildWhereSql(MDataPaginationReq request) {
        return new WhereSql()
                .andOnNotEmpty("\"id\" = ? ", request.getId())
                .andOnNotEmpty("\"identity\" = ?", request.getIdentity())
                .andBetween(Delim.ORACLE, "timestamp", request.getStartTime(), request.getEndTime());
    }

}
