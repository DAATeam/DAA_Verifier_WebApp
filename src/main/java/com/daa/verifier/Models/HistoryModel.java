package com.daa.verifier.Models;

/**
 * Created by DK on 1/13/17.
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;

public class HistoryModel {
    private Integer serviceId;
    private String userInfo;
    private Boolean result;
    private Timestamp timestamp;

    public HistoryModel(Integer serviceId, String userInfo, Boolean result, Timestamp timestamp) {
        this.serviceId = serviceId;
        this.userInfo = userInfo;
        this.result = result;
        this.timestamp = timestamp;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public HistoryModel() {
    }

    public JSONObject toJson() {
        JSONObject json =  new JSONObject();
        json.put("serviceId", this.getServiceId());
        json.put("userInfo", this.getUserInfo());
        json.put("result", this.getResult());
        json.put("timestamp", this.getTimestamp());
        return  json;
    };
}
