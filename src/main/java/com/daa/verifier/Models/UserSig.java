package com.daa.verifier.Models;

import org.json.JSONObject;

/**
 * Created by DK on 12/28/16.
 */
public class UserSig {
    private String status;
    private String sig;
    private JSONObject information;

    public UserSig() {
    }

    public UserSig(String status, String sig, JSONObject information) {
        this.status = status;
        this.sig = sig;
        this.information = information;
    }

    public UserSig(String stringJson) {
        try {
            JSONObject json = new JSONObject(stringJson);
            this.status = json.getString("status");
            this.sig = json.getString("sig");
            this.information = json.getJSONObject("information");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public JSONObject getInformation() {
        return information;
    }

    public void setInformation(JSONObject information) {
        this.information = information;
    }
}
