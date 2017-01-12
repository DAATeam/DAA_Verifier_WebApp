package com.daa.verifier.Repository;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.daa.verifier.Models.HistoryModel;
import java.util.ArrayList;
/**
 * Created by DK on 1/13/17.
 */
public class HistoryModelRowMapper implements RowMapper {
    public HistoryModelRowMapper() {
    }

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        HistoryModel history = new HistoryModel();
        history.setServiceId(rs.getInt("service_id"));
        history.setUserInfo(rs.getString("user_info"));
        history.setResult(rs.getBoolean("result_verify"));
        history.setTimestamp(rs.getTimestamp("timestamp"));
        return history;
    }

    public List<HistoryModel> mapMultiRows(List<Map<String, Object>> rows) {
        List<HistoryModel> historyModels = new ArrayList<HistoryModel>();
        for (Map row : rows) {
            HistoryModel history = new HistoryModel();
            history.setServiceId((Integer) row.get("service_id"));
            history.setUserInfo((String) row.get("user_info"));
            history.setResult((Boolean) row.get("result_verify"));
            history.setTimestamp((Timestamp) row.get("timestamp"));
            historyModels.add(history);
        }
        return historyModels;
    }
}
