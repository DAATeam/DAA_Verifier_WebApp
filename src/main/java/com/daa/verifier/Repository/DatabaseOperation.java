package com.daa.verifier.Repository;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.daa.verifier.Models.VerifierSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

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

    public boolean checkAppIdExisted(VerifierSignature verifierSignature) throws SQLException {
        System.out.println("AppId to check Exist: " + verifierSignature.getAppId());
        String sql = "select count(*) from signature where app_id=:appId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("appId", verifierSignature.getAppId());
        int count = this.jdbcTemplate.queryForObject(
                sql,
                namedParameters, Integer.class);
        System.out.println("Result Count: "+count);

        return count != 0;
    }

    public boolean addCertificate( VerifierSignature verifierSignature) throws SQLException {
        System.out.println("add Cert: "+verifierSignature.toString());
        if(checkAppIdExisted(verifierSignature)) {
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
}
