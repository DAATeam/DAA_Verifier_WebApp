package com.daa.verifier.Repository;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.daa.verifier.Models.HistoryModel;
import com.daa.verifier.Models.VerifierSignature;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import java.util.List;

import javax.sql.DataSource;

/**
 * Created by DK on 12/17/16.
 */
@Repository
public class DatabaseOperation {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public DatabaseOperation(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    public DatabaseOperation() {}

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public boolean checkAppIdExisted(String appId) throws SQLException {
        System.out.println("AppId to check Exist: " +appId);
        String sql = "select count(*) from signature where app_id=:appId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("appId", appId);
        int count = this.jdbcTemplate.queryForObject(
                sql,
                namedParameters, Integer.class);
        System.out.println("Result Count: "+count);

        return count != 0;
    }

    public String getAppData(Integer appId) throws SQLException {
        String sql = "select data from signature where app_id=:appId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("appId", appId);
        return this.jdbcTemplate.queryForObject(
                sql,
                namedParameters, String.class);
    }

    public boolean addCertificate(VerifierSignature verifierSignature) throws SQLException {
        System.out.println("add Cert: "+verifierSignature.toString());
        if(checkAppIdExisted(String.valueOf(verifierSignature.getAppId()))) {
            String sql = "update signature set data=:data where app_id=:appId";
            Map namedParameters = new HashMap();
            namedParameters.put("appId", verifierSignature.getAppId());
            namedParameters.put("data", verifierSignature.getData());
            int update = this.jdbcTemplate.update(sql, namedParameters);
            System.out.println("add Cert update Result: "+update);
        } else {
            String sql = "insert INTO signature(app_id,data) VALUES (:appId,:data)";
            Map namedParameters = new HashMap();
            namedParameters.put("appId", verifierSignature.getAppId());
            namedParameters.put("data", verifierSignature.getData());
            int insert = this.jdbcTemplate.update(sql, namedParameters);
            System.out.println("add Cert insert Result: "+insert);
        }
        return true;
    }

    public boolean addVerifyLog( Integer serviceId, String userInfo, Boolean result) {
        String sql = "insert INTO history(service_id,user_info,result_verify) VALUES (:appId,:info,:result)";
        Map namedParameters = new HashMap();
        namedParameters.put("appId", serviceId);
        namedParameters.put("info", userInfo);
        namedParameters.put("result", result);
        int insert = this.jdbcTemplate.update(sql, namedParameters);
        System.out.println("insert result "+ insert);
        return insert != 0;
    }

    public List<HistoryModel> getVerifyLog(Integer serviceId) {
        String sql = "SELECT * FROM history WHERE service_id=:serviceId";
        Map namedParameters = new HashMap();
        namedParameters.put("serviceId", serviceId);
        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql,namedParameters);
        HistoryModelRowMapper mapper = new HistoryModelRowMapper();
        return mapper.mapMultiRows(rows);
    }
}
