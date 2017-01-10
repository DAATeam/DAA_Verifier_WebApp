package com.daa.verifier.Models;

import org.json.JSONObject;

/**
 * Created by DK on 1/10/17.
 */
public class ServiceSig {
    private String status;
    private String sig;
    private JSONObject permission;
    private String sessionId;

    public ServiceSig() {
    }

    public ServiceSig(String status, String sig, JSONObject permission, String sessionId) {
        this.status = status;
        this.sig = sig;
        this.permission = permission;
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public JSONObject getPermission() {
        return permission;
    }

    public void setPermission(JSONObject permission) {
        this.permission = permission;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
